package ro.tuc.ds2020.controllers.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

/**
 * Exception thrown when authentication fails (invalid username or password).
 * Results in HTTP 401 Unauthorized.
 */
public class AuthenticationException extends CustomException {

    private static final String DEFAULT_MESSAGE = "Invalid username or password";
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    /**
     * Constructs an AuthenticationException with default message.
     */
    public AuthenticationException() {
        super(DEFAULT_MESSAGE, HTTP_STATUS, "UserCredential", new ArrayList<>());
    }

    /**
     * Constructs an AuthenticationException with a custom message.
     *
     * @param message the failure detail message
     */
    public AuthenticationException(final String message) {
        super(message, HTTP_STATUS, "UserCredential", new ArrayList<>());
    }
}
