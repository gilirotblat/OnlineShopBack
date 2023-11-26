package gilir.backendfinalpro.error;


import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class ShopExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)//404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {

        var problemDetails = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());

        problemDetails.setProperty("timestamp", LocalDateTime.now());
        problemDetails.setProperty("resourceName", e.getResourceName());
        problemDetails.setProperty("resourceId", e.getResourceId());

        return problemDetails;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)//400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        //new ProblemDetail Object:
        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation Failed");

        //for Each fieldError in the exception:
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            problemDetail.setProperty("Validation Failed for property", fieldError.getField());
            //problemDetail.setProperty("objectName", fieldError.getObjectName());
            problemDetail.setProperty("message", fieldError.getDefaultMessage());
            problemDetail.setProperty("rejectedValue", fieldError.getRejectedValue());
        });

        //add details about the exception:
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)//400
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.BAD_REQUEST, "Database save Failed");

        if (e.getCause() instanceof ConstraintViolationException) {
            problemDetail.setProperty("cause", "Constraint Violation");
        }
        //add details about the exception:
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//500
    @ExceptionHandler(ShopException.class)
    public ProblemDetail handleShopException(ShopException e) {
        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        var problemDetail =
                ProblemDetail.
                        forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}