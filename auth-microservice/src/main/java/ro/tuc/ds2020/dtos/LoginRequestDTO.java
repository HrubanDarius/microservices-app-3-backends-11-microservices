package ro.tuc.ds2020.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO used to carry login credentials from the client to the auth service.
 */
public class LoginRequestDTO {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    /** No-arg constructor. */
    public LoginRequestDTO() {
    }

    /**
     * Full constructor.
     *
     * @param username the username
     * @param password the plain-text password
     */
    public LoginRequestDTO(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns the password.
     *
     * @return plain-text password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
