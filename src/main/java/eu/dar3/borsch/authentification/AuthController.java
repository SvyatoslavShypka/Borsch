package eu.dar3.borsch.authentification;

import eu.dar3.borsch.errors.ErrorMessages;
import eu.dar3.borsch.errors.InfoMessages;
import eu.dar3.borsch.mail.EmailService;
import eu.dar3.borsch.user.User;
import eu.dar3.borsch.user.UserRepository;
import eu.dar3.borsch.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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
    private final UserRepository userRepository;
    private final ResourceBundle resourceBundle;

    @GetMapping("/login")
    public String getLoginPage(Model model) {
//        model.addAttribute("pp", resourceBundle.getString("page.login.title"));
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
                errorsMessages.addError(resourceBundle.getString("information.message.yet_registered"));
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
                infoMessages.addMessage(resourceBundle.getString("information.message.yet_registered"));
            } else {
                if (code == user.getCode()) {
                    Calendar cal1 = Calendar.getInstance(); // creates calendar
                    cal1.setTime(Date.from(user.getCodeDate()));               // sets calendar time/date
                    cal1.setTimeZone(TimeZone.getTimeZone("UTC"));
                    cal1.add(Calendar.HOUR_OF_DAY, CODE_LIFE_CYCLE);      // adds one hour
                    Calendar cal2 = Calendar.getInstance(); // creates calendar
                    cal2.setTimeZone(TimeZone.getTimeZone("UTC"));
                    if (cal1.getTime().toInstant().compareTo(cal2.toInstant()) < 0) {
                        errorsMessages.addError(resourceBundle.getString("information.error.overdue_code"));
                        sendConfirmation(user.getEmail(), user.getNickname(), errorsMessages, infoMessages);
                        return "user/register-finish";
                    }
                    user.setEnable(true);
                    userRepository.save(user);
                    infoMessages.addMessage(resourceBundle.getString("information.message.yet_registered"));
                } else {
                    errorsMessages.addError(resourceBundle.getString("information.error.invalid_code"));
                    return "user/register-finish";
                }
            }
            return "user/login";
        } catch (NoSuchElementException e) {
            errorsMessages.addError(resourceBundle.getString("information.message.not_registered"));
            return "user/register";
        }
    }

    private void sendConfirmation(String username, String nickname,
                                  ErrorMessages errorsMessages, InfoMessages infoMessages) {
        infoMessages.addMessage(resourceBundle.getString("page.register.confirmation.message1")
                + username + resourceBundle.getString("page.register.confirmation.message2"));
        User user = userService.findUserByName(username);
        int codeInt = codeGeneration();
        user.setCode(codeInt);
        userRepository.save(user);
        String code = String.valueOf(codeInt);
        emailService.sendEmail(username, resourceBundle.getString( "email.subject.message"),
                resourceBundle.getString("email.text.message1") + nickname
                        + resourceBundle.getString("email.text.message2")
                        + code
                        + resourceBundle.getString("email.text.message3"));
    }

    private int codeGeneration() {
        Random rand = new Random();
        return rand.nextInt(CODE_FINISH - CODE_START) + CODE_START;
    }
}
