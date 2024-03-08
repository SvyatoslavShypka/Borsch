package eu.dar3.borsch.authentification;

import eu.dar3.borsch.errors.ErrorMessages;
import eu.dar3.borsch.errors.InfoMessages;
import eu.dar3.borsch.mail.EmailService;
import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserDto;
import eu.dar3.borsch.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.UUID;

//@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    private Properties properties;

    @GetMapping("/login")
    public String getLoginPage() {
        return "user/login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "user/register";
    }

    @PostMapping("/sendEmailConfirmation")
    public String sendEmailConfirmation(@ModelAttribute("errorsMessages") ErrorMessages errorsMessages,
                                        @ModelAttribute("infoMessages") InfoMessages infoMessages,
                                        @RequestParam(value = "username") String username) {

        infoMessages.addMessage("На Вашу поштову скриньку: " + username +
                " було відправлено код для підтвердження. Просимо перевірити скриньку і ввести цей код");
        String code = UUID.randomUUID().toString();
        emailService.sendEmail(username, "Borsch message","You have just registered to Borsch application. " +
                        "Please use this code to finish your registration: " + code +
                        "\nIn case you have not registered to Borsch application please ignore this e-mail");

        return "user/register";
    }




    @PostMapping("/register")
    public String registerUser(@ModelAttribute("errorsMessages") ErrorMessages errorsMessages,
                               @ModelAttribute("infoMessages") InfoMessages infoMessages,
                               @RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "nickname") String nickname) {
        try {
            User user = userService.findUserByName(username);
            if (user.isEnable()) {
                errorsMessages.addError("Ви вже зареєстровані і можете залогуватися");
            } else {
                sendConfirmation(user, errorsMessages, infoMessages);
                }
            return "user/login";
        } catch (NoSuchElementException e) {
            userService.createNewUser(username, password, nickname);
            User user = userService.findUserByName(username);
            user.setEnable(false);
            sendConfirmation(user, errorsMessages, infoMessages);
//TODO change message
//            infoMessages.addMessage("Успішна реєстрація. Можете залогуватися");
            return "user/login";
        }
    }

    private void sendConfirmation(User user, ErrorMessages errorsMessages, InfoMessages infoMessages) {
        infoMessages.addMessage("На Вашу поштову скриньку: " + user.getEmail() +
                " було відправлено прохання підтвердити Ваш e-mail");
        String link = linkGeneration(user.getUserId());
        emailService.sendEmail(user.getEmail(), "Email_subject",
                "Hello " + user.getNickname() + "! Please confirm your e-mail by clicking link below: \n" + link);
    }

    private String linkGeneration(UUID userId) {
//        logger.info("linkGenerator");
        System.out.println("linkGeneration...");
        StringBuilder link = new StringBuilder(properties.getProperty("server.url"));
        link.append(":").append(properties.getProperty("server.port"));
        link.append("/account/confirmation?email=").append(userService.findUserById(userId).getEmail());
        link.append("&code=").append(passwordEncoder.encode(userId.toString()));
        return link.toString();
    }

}
