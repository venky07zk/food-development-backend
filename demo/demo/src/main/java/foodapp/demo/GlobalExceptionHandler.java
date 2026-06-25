package foodapp.demo;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FoodNotFound.class)
    public ResponseEntity<String> handlefoodnotfound(FoodNotFound f)
    {
        return ResponseEntity.status(404).body(f.getMessage());
    }
    @ExceptionHandler(RestaurentNotFoundException.class)
    public ResponseEntity<String> handlerestaurentnotfound(RestaurentNotFoundException r)
    {
        return ResponseEntity.status(404).body(r.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidation(
            ConstraintViolationException ex)
    {
        return ResponseEntity
                .badRequest()
                .body("Validation Error: " + ex.getMessage());
    }
}