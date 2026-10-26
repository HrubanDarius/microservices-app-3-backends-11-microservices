package ro.tuc.ds2020.controllers.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

/**
 * Exception thrown when a resource already exists (e.g. username taken).
 */
public class DuplicateResourceException extends CustomException {

    private static final String DEFAULT_MESSAGE = "Resource duplicated!";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    /**
     * Constructs a DuplicateResourceException for the given resource.
     *
     * @param resource the identifier or description of the duplicated resource
     */
    public DuplicateResourceException(final String resource) {
        super(DEFAULT_MESSAGE, HTTP_STATUS, resource, new ArrayList<>());
    }

    /**
     * Constructs a DuplicateResourceException with a custom message.
     *
     * @param message  custom error message
     * @param resource the resource name
     */
    public DuplicateResourceException(final String message, final String resource) {
        super(message, HTTP_STATUS, resource, new ArrayList<>());
    }
}
