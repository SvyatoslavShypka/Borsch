package eu.dar3.borsch.errors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecipeValidationException.class)
    public ModelAndView handleValidateException(HttpServletRequest req, RecipeValidationException ex) {
        ModelAndView result = new ModelAndView();
        result.addObject("errorsMessages", ex.getErrorMessages());
        result.setViewName("error/400-bad-request");
        return result;
    }
}
