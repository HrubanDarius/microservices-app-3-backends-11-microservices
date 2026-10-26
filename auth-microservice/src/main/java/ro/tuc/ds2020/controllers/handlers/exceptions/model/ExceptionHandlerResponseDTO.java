package ro.tuc.ds2020.controllers.handlers.exceptions.model;

import java.util.Collection;
import java.util.Date;

/**
 * Standard error response DTO returned to clients when an exception occurs.
 */
public class ExceptionHandlerResponseDTO {

    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String resource;
    private Collection<?> details;

    /** No-arg constructor. */
    public ExceptionHandlerResponseDTO() {
        this.timestamp = new Date();
    }

    /**
     * Full constructor.
     *
     * @param resource the affected resource
     * @param error    the error reason phrase
     * @param status   the HTTP status code
     * @param message  the detail message
     * @param details  sub-details or validation error list
     * @param path     the request path
     */
    public ExceptionHandlerResponseDTO(final String resource, final String error,
                                      final int status, final String message,
                                      final Collection<?> details, final String path) {
        this.timestamp = new Date();
        this.resource = resource;
        this.error = error;
        this.status = status;
        this.message = message;
        this.details = details;
        this.path = path;
    }

    /**
     * Returns the timestamp.
     *
     * @return timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the HTTP status code.
     *
     * @return status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param status the status to set
     */
    public void setStatus(final int status) {
        this.status = status;
    }

    /**
     * Returns the error reason.
     *
     * @return error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error reason.
     *
     * @param error the error to set
     */
    public void setError(final String error) {
        this.error = error;
    }

    /**
     * Returns the message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the message to set
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Returns the request path.
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the request path.
     *
     * @param path the path to set
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Returns the resource name.
     *
     * @return resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the resource name.
     *
     * @param resource the resource to set
     */
    public void setResource(final String resource) {
        this.resource = resource;
    }

    /**
     * Returns details collection.
     *
     * @return details
     */
    public Collection<?> getDetails() {
        return details;
    }

    /**
     * Sets details collection.
     *
     * @param details details to set
     */
    public void setDetails(final Collection<?> details) {
        this.details = details;
    }
}
