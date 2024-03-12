package eu.dar3.borsch.user;

import eu.dar3.borsch.errors.ErrorMessages;
import eu.dar3.borsch.errors.RecipeValidationException;
import eu.dar3.borsch.utils.Util;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final Properties properties;

    private final ErrorMessages errorMessages = new ErrorMessages();
    @Setter
    private UserService userService;

    private final ResourceBundle resourceBundle;


    public void validate(UserDto userDto, boolean newUser) {
        errorMessages.clear();
        checkPassword(userDto.getPassword());
        checkEmail(userDto.getEmail(), newUser);


        if (!errorMessages.getErrors().isEmpty()) {
            throw new RecipeValidationException(errorMessages);
        }
    }

    private void checkEmail(String email, boolean newUser) {
        if (StringUtils.isBlank(email)) {
            errorMessages.addError("Не введено e-mail!");
        } else {
            if (newUser) {
                try {
                    userService.findUserByName(email);
                    errorMessages.addError("Користувач з e-mail " + email + " вже зареєстрований");
                } catch (NoSuchElementException ex) {
                    //No action
                }
            }
        }
    }


    private void checkPassword(String password) {
        if (StringUtils.isBlank(password)) {
            errorMessages.addError("Не введено пароля!");
        }

        String passwordRegexPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})";
        if (!Util.patternMatches(password, passwordRegexPattern)) {
            //TODO to extend
            System.out.println("resourceBundle.getString(\"page.login.password.input.validator\") = " + resourceBundle.getString("page.login.password.input.validator"));
            errorMessages.addError(resourceBundle.getString("page.login.password.input.validator"));
        }
    }
}
