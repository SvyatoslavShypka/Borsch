package eu.dar3.borsch.errors;

public class RecipeValidationException extends RuntimeException {
    private ErrorMessages errorMessages;

    public RecipeValidationException(String message) {
        super(message);
    }

    public RecipeValidationException(ErrorMessages errorMessages) {
        this.errorMessages = errorMessages;
    }

    public ErrorMessages getErrorMessages() {
        return errorMessages;
    }
}
