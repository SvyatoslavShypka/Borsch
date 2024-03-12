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
            errorMessages.addError(resourceBundle.getString("page.login.email.input.empty"));
        } else {
            if (newUser) {
                try {
                    userService.findUserByName(email);
                    errorMessages.addError(resourceBundle.getString("information.error.yet_registered1")
                            + email + resourceBundle.getString("information.error.yet_registered2"));
                } catch (NoSuchElementException ex) {
                    //No action
                }
            }
        }
    }

    private void checkPassword(String password) {
        if (StringUtils.isBlank(password)) {
            errorMessages.addError(resourceBundle.getString("page.login.password.input.empty"));
        }
        String passwordRegexPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})";
        if (!Util.patternMatches(password, passwordRegexPattern)) {
            errorMessages.addError(resourceBundle.getString("page.login.password.input.validator"));
        }
    }
}
