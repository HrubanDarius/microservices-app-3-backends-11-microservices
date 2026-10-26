package ro.tuc.ds2020.controllers.handlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.CustomException;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ExceptionHandlerResponseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Global REST exception handler that formats errors into a unified {@link ExceptionHandlerResponseDTO}.
 */
@ControllerAdvice
@SuppressWarnings("checkstyle:DesignForExtension")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(
            final ConstraintViolationException ex, final WebRequest request) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final List<String> details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();
        final ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(
                ConstraintViolationException.class.getSimpleName(),
                status.getReasonPhrase(),
                status.value(),
                ex.getMessage(),
                details,
                request.getDescription(false)
        );
        return handleExceptionInternal(ex, errorInformation, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final List<String> details = new ArrayList<>();
        for (final ObjectError err : ex.getBindingResult().getAllErrors()) {
            final String name = (err instanceof FieldError fieldError)
                    ? fieldError.getField()
                    : err.getObjectName();
            details.add(name + ": " + err.getDefaultMessage());
        }
        final ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(
                ex.getParameter().getParameterName(),
                HttpStatus.valueOf(status.value()).getReasonPhrase(),
                status.value(),
                MethodArgumentNotValidException.class.getSimpleName(),
                details,
                request.getDescription(false)
        );
        return handleExceptionInternal(ex, errorInformation, headers, status, request);
    }

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<Object> handleCustom(
            final CustomException ex, final WebRequest request) {
        final ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(
                ex.getResource(),
                ex.getStatus().getReasonPhrase(),
                ex.getStatus().value(),
                ex.getMessage(),
                ex.getValidationErrors(),
                request.getDescription(false)
        );
        return handleExceptionInternal(ex, errorInformation, new HttpHeaders(), ex.getStatus(), request);
    }
}
