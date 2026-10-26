package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.UserCredential;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link UserCredential} entities.
 * Provides standard CRUD operations plus username-based lookup methods.
 */
public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {

    /**
     * Finds a credential record by username.
     *
     * @param username the username to search for
     * @return an Optional containing the credential if found
     */
    Optional<UserCredential> findByUsername(String username);

    /**
     * Checks whether a credential record already exists for the given username.
     *
     * @param username the username to check
     * @return true if the username is already taken, false otherwise
     */
    boolean existsByUsername(String username);
}
