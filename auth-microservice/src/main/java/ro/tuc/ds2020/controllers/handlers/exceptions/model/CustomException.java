package ro.tuc.ds2020.controllers.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Base custom runtime exception containing HTTP status and validation details.
 */
@SuppressWarnings("checkstyle:DesignForExtension")
public class CustomException extends RuntimeException {

    private final String resource;
    private final HttpStatus status;
    private final List<String> validationErrors;

    /**
     * Constructs a new CustomException.
     *
     * @param message          the detail message
     * @param status           the HTTP status
     * @param resource         the affected resource name
     * @param validationErrors additional validation error descriptions
     */
    public CustomException(final String message, final HttpStatus status,
                           final String resource, final List<String> validationErrors) {
        super(message);
        this.resource = resource;
        this.status = status;
        this.validationErrors = validationErrors;
    }

    /**
     * Returns the affected resource name.
     *
     * @return resource name
     */
    public String getResource() {
        return resource;
    }

    /**
     * Returns the HTTP status associated with this exception.
     *
     * @return HTTP status
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Returns the list of validation errors.
     *
     * @return validation errors list
     */
    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
