package eu.dar3.borsch.recipe;

import eu.dar3.borsch.errors.ErrorMessages;
import eu.dar3.borsch.errors.NoteValidationException;
import org.springframework.stereotype.Component;

import static eu.dar3.borsch.utils.Constants.*;

@Component
public class RecipeValidator {

    private final ErrorMessages errorMessages = new ErrorMessages();

    public void validate(RecipeDto recipeDto) {
        errorMessages.clear();
        checkTitle(recipeDto.getTitle());

        if (!errorMessages.getErrors().isEmpty()) {
            throw new NoteValidationException(errorMessages);
        }
    }

    private void checkTitle(String title) {
        if (title == null || title.isEmpty()) {
            errorMessages.addError("Заголовок не може бути пустим.");
        }
        else {
            if (title.length() > RECIPE_TITLE_MAX_LENGTH || title.length() < RECIPE_TITLE_MIN_LENGTH) {
                errorMessages.addError("Довжина заголовка повинна бути від " + RECIPE_TITLE_MIN_LENGTH
                        + " до " + RECIPE_TITLE_MAX_LENGTH + " символів");
            }
        }
    }
}
