package ro.tuc.ds2020.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ro.tuc.ds2020.entities.Role;

import java.util.UUID;

/**
 * DTO used to carry registration data from the client to the auth service.
 * Supports an optional explicit UUID id to synchronize with user-microservice.
 */
public class RegisterRequestDTO {

    private UUID id;

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
     * Constructor without id.
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
     * Full constructor including optional UUID id.
     *
     * @param id       the explicit user UUID (optional)
     * @param username the chosen username
     * @param password the plain-text password
     * @param role     the assigned role
     */
    public RegisterRequestDTO(final UUID id, final String username,
                              final String password, final Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Returns the optional explicit UUID.
     *
     * @return user UUID or null
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the explicit UUID.
     *
     * @param id the UUID to set
     */
    public void setId(final UUID id) {
        this.id = id;
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
