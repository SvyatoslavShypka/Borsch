package eu.dar3.borsch.user;

import eu.dar3.borsch.errors.ErrorMessages;
import eu.dar3.borsch.errors.RecipeValidationException;
import eu.dar3.borsch.utils.Util;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final ErrorMessages errorMessages = new ErrorMessages();
    private final MessageSource messageSource;
    private final Locale locale = new Locale("pl");

    @Setter
    private UserService userService;

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
            errorMessages.addError(messageSource.getMessage("page.login.email.input.empty", null, locale));
        } else {
            if (newUser) {
                try {
                    userService.findUserByName(email);
                    errorMessages.addError(messageSource.getMessage("information.error.yet_registered1", null, locale)
                            + email + messageSource.getMessage("information.error.yet_registered2", null, locale));
                } catch (NoSuchElementException ex) {
                    //No action
                }
            }
        }
    }

    private void checkPassword(String password) {
        if (StringUtils.isBlank(password)) {
            errorMessages.addError(messageSource.getMessage("page.login.password.input.empty", null, locale));
        }
        String passwordRegexPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})";
        if (!Util.patternMatches(password, passwordRegexPattern)) {
            errorMessages.addError(messageSource.getMessage("page.login.password.input.validator", null, locale));
        }
    }
}
