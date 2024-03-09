package eu.dar3.borsch.authentification;

import eu.dar3.borsch.errors.ErrorMessages;
import eu.dar3.borsch.errors.InfoMessages;
import eu.dar3.borsch.mail.EmailService;
import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserDto;
import eu.dar3.borsch.user.UserRepository;
import eu.dar3.borsch.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.MappingMatch;
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
    private final UserRepository userRepository;

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
                                        @RequestParam(value = "nickname") String nickname,
                                        @RequestParam(value = "username") String username,
                                        @RequestParam(value = "password") String password) {
        try {
            User user = userService.findUserByName(username);
            if (user.isEnable()) {
                errorsMessages.addError("Ви вже зареєстровані і можете залогуватися");
                return "user/login";
            } else {
                sendConfirmation(username, nickname, errorsMessages, infoMessages);
            }
            return "user/register-finish";
        } catch (NoSuchElementException e) {
            userService.createNewUser(username, password, nickname);
            User user = userService.findUserByName(username);
            user.setEnable(true);
            sendConfirmation(username, nickname, errorsMessages, infoMessages);
            return "user/register-finish";
        }
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("errorsMessages") ErrorMessages errorsMessages,
                               @ModelAttribute("infoMessages") InfoMessages infoMessages,
                               @RequestParam(value = "username") String username,
                               @RequestParam(value = "code") int code){
        try {
            User user = userService.findUserByName(username);
            if (user.isEnable()) {
                errorsMessages.addError("Ви вже зареєстровані і можете залогуватися");
            } else {
                if (code == user.getCode()) {
                    user.setEnable(true);
                } else {
                    errorsMessages.addError("Невірний або неактуальний код");
                    return "user/register-finish";
                }
            }
            return "user/login";
        } catch (NoSuchElementException e) {
            errorsMessages.addError("Ви не є зареєстровані - прошу зареєструватися");
            return "user/register";
        }
    }

    private void sendConfirmation(String username, String nickname,
                                  ErrorMessages errorsMessages, InfoMessages infoMessages) {
        infoMessages.addMessage("На Вашу поштову скриньку: " + username +
                " було відправлено код для підтвердження Вашого e-mail");
        User user = userService.findUserByName(username);
        int codeInt = codeGeneration();
        user.setCode(codeInt);
        userRepository.save(user);
        String code = String.valueOf(codeInt);
        emailService.sendEmail(username, "Borsch e-mail confirmation",
                "Hello " + nickname + "! You have just registered to Borsch application. \n" +
                        "Please use this code for confirmation the registration on Borsch page: \n" + code
                        + "\nIn case you have not registered to Borsch application please ignore this e-mail");
    }

    private int codeGeneration() {
//        logger.info("linkGenerator");
        System.out.println("linkGeneration...");
        return 1234;
    }

}
