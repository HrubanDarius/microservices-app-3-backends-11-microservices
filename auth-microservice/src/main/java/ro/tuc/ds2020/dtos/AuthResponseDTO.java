package ro.tuc.ds2020.dtos;

import java.util.UUID;

/**
 * DTO returned by the auth service after a successful login or registration.
 * Contains the JWT token and basic user identity information.
 */
public class AuthResponseDTO {

    private String token;
    private UUID id;
    private String username;
    private String role;

    /** No-arg constructor. */
    public AuthResponseDTO() {
    }

    /**
     * Full constructor.
     *
     * @param token    the signed JWT token
     * @param id       the user's UUID
     * @param username the user's username
     * @param role     the user's role name (ADMIN or CLIENT)
     */
    public AuthResponseDTO(final String token, final UUID id,
                           final String username, final String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.role = role;
    }

    /**
     * Returns the JWT token.
     *
     * @return JWT token string
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the JWT token.
     *
     * @param token the token to set
     */
    public void setToken(final String token) {
        this.token = token;
    }

    /**
     * Returns the user UUID.
     *
     * @return user UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the user UUID.
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
     * Returns the role name.
     *
     * @return role as string
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role name.
     *
     * @param role the role to set
     */
    public void setRole(final String role) {
        this.role = role;
    }
}
