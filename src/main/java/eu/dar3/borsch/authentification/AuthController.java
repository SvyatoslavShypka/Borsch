package eu.dar3.borsch.authentification;

import eu.dar3.borsch.errors.ErrorMessages;
import eu.dar3.borsch.errors.InfoMessages;
import eu.dar3.borsch.mail.EmailService;
import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserRepository;
import eu.dar3.borsch.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Properties;
import java.util.NoSuchElementException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Random;

import static eu.dar3.borsch.utils.Constants.CODE_LIFE_CYCLE;
import static eu.dar3.borsch.utils.Constants.CODE_FINISH;
import static eu.dar3.borsch.utils.Constants.CODE_START;

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
            sendConfirmation(username, nickname, errorsMessages, infoMessages);
            return "user/register-finish";
        }
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("errorsMessages") ErrorMessages errorsMessages,
                               @ModelAttribute("infoMessages") InfoMessages infoMessages,
                               @RequestParam(value = "username") String username,
                               @RequestParam(value = "code") int code) {
        try {
            User user = userService.findUserByName(username);
            if (user.isEnable()) {
                infoMessages.addMessage("Ви вже зареєстровані і можете залогуватися");
            } else {
                if (code == user.getCode()) {
                    Calendar cal1 = Calendar.getInstance(); // creates calendar
                    cal1.setTime(Date.from(user.getCodeDate()));               // sets calendar time/date
                    cal1.setTimeZone(TimeZone.getTimeZone("UTC"));
                    cal1.add(Calendar.HOUR_OF_DAY, CODE_LIFE_CYCLE);      // adds one hour
                    Calendar cal2 = Calendar.getInstance(); // creates calendar
                    cal2.setTimeZone(TimeZone.getTimeZone("UTC"));
                    if (cal1.getTime().toInstant().compareTo(cal2.toInstant()) < 0) {
                        errorsMessages.addError("Код вже неактуальний.");
                        sendConfirmation(user.getEmail(), user.getNickname(), errorsMessages, infoMessages);
                        return "user/register-finish";
                    }
                    user.setEnable(true);
                    userRepository.save(user);
                    infoMessages.addMessage("Реєстрацію підтверджено. Ви можете залогуватися");
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
        infoMessages.addMessage("На Вашу поштову скриньку: " + username
                + " було відправлено код для підтвердження Вашого e-mail");
        User user = userService.findUserByName(username);
        int codeInt = codeGeneration();
        user.setCode(codeInt);
        userRepository.save(user);
        String code = String.valueOf(codeInt);
        emailService.sendEmail(username, "Borsch e-mail confirmation",
                "Hello " + nickname + "! You have just registered to Borsch page. \n"
                        + "Please use this code for confirmation the registration on Borsch page: \n"
                        + code
                        + "\nIn case you have not registered to Borsch application please ignore this e-mail");
    }

    private int codeGeneration() {
//        logger.info("linkGenerator");
        Random rand = new Random();
        return rand.nextInt(CODE_FINISH - CODE_START) + CODE_START;
    }

}
