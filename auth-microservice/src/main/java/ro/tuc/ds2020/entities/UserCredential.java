package ro.tuc.ds2020.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;
import java.util.UUID;

/**
 * JPA entity that stores user authentication credentials.
 * Passwords are stored as BCrypt hashes, never in plain text.
 */
@Entity
@Table(name = "user_credentials")
@SuppressWarnings("checkstyle:DesignForExtension")
public class UserCredential {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /** No-arg constructor required by JPA. */
    public UserCredential() {
    }

    /**
     * Full constructor for creating a new credential entity.
     *
     * @param username the unique username
     * @param password the BCrypt-hashed password
     * @param role     the user's role
     */
    public UserCredential(final String username, final String password, final Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Returns the UUID primary key.
     *
     * @return user UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the UUID primary key.
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
     * Returns the BCrypt-hashed password.
     *
     * @return hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the BCrypt-hashed password.
     *
     * @param password the hashed password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Returns the user's role.
     *
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the user's role.
     *
     * @param role the role to set
     */
    public void setRole(final Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserCredential)) {
            return false;
        }
        final UserCredential other = (UserCredential) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
