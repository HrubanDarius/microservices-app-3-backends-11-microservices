package ro.tuc.ds2020.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ro.tuc.ds2020.entities.Role;

/**
 * DTO used to carry registration data from the client to the auth service.
 */
public class RegisterRequestDTO {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotNull(message = "Role must not be null")
    private Role role;

    /** No-arg constructor. */
    public RegisterRequestDTO() {
    }

    /**
     * Full constructor.
     *
     * @param username the chosen username
     * @param password the plain-text password (will be hashed)
     * @param role     the assigned role
     */
    public RegisterRequestDTO(final String username, final String password, final Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
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

    /**
     * Returns the role.
     *
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role the role to set
     */
    public void setRole(final Role role) {
        this.role = role;
    }
}
