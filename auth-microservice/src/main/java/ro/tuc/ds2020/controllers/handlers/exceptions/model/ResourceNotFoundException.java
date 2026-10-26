package ro.tuc.ds2020.controllers.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

/**
 * Exception thrown when an expected entity/resource is not found.
 */
public class ResourceNotFoundException extends CustomException {

    private static final String DEFAULT_MESSAGE = "Resource not found!";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    /**
     * Constructs a ResourceNotFoundException for the given resource.
     *
     * @param resource description of the missing resource
     */
    public ResourceNotFoundException(final String resource) {
        super(DEFAULT_MESSAGE, HTTP_STATUS, resource, new ArrayList<>());
    }
}
