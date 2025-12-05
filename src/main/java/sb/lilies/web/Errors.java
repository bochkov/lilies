package sb.lilies.web;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class Errors {

    public record ErrorMessage(String message) {
    }

    @ExceptionHandler(value = EntityNotFoundException.class, produces = "text/html")
    public String handle404(EntityNotFoundException ex, Model model) {
        return "error/404";
    }

    @ExceptionHandler(value = EntityNotFoundException.class, produces = "application/json")
    public ResponseEntity<ErrorMessage> handle404Rest(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(value = Exception.class, produces = "text/html")
    public String handle500(Exception ex, Model model) {
        return "error/500";
    }

    @ExceptionHandler(value = Exception.class, produces = "application/json")
    public ResponseEntity<ErrorMessage> handle500Rest(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(ex.getMessage()));
    }
}
