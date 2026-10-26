package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.AuthenticationException;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.DuplicateResourceException;
import ro.tuc.ds2020.dtos.AuthResponseDTO;
import ro.tuc.ds2020.dtos.LoginRequestDTO;
import ro.tuc.ds2020.dtos.RegisterRequestDTO;
import ro.tuc.ds2020.entities.UserCredential;
import ro.tuc.ds2020.repositories.UserCredentialRepository;
import ro.tuc.ds2020.security.JwtTokenProvider;

import java.util.UUID;

/**
 * Service orchestrating user registration, authentication, and JWT issuance.
 */
@Service
@SuppressWarnings("checkstyle:DesignForExtension")
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserCredentialRepository userCredentialRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Injects dependencies required for authentication operations.
     *
     * @param userCredentialRepository repository for credentials database
     * @param passwordEncoder          BCrypt encoder for secure password hashing
     * @param jwtTokenProvider         provider for JWT token creation and validation
     */
    @Autowired
    public AuthService(final UserCredentialRepository userCredentialRepository,
                       final BCryptPasswordEncoder passwordEncoder,
                       final JwtTokenProvider jwtTokenProvider) {
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user account with hashed password and generates an initial JWT token.
     * If an explicit UUID is provided in dto.getId(), it sets that ID to synchronize identity.
     *
     * @param dto the registration details
     * @return AuthResponseDTO containing JWT token and user info
     * @throws DuplicateResourceException if the username is already registered
     */
    @Transactional
    public AuthResponseDTO register(final RegisterRequestDTO dto) {
        if (userCredentialRepository.existsByUsername(dto.getUsername())) {
            LOGGER.error("Registration failed: username '{}' already exists", dto.getUsername());
            throw new DuplicateResourceException("Username '" + dto.getUsername() + "' is already taken!");
        }

        final UUID credentialId = dto.getId() != null ? dto.getId() : UUID.randomUUID();
        final String hashedPassword = passwordEncoder.encode(dto.getPassword());
        final UserCredential credential = new UserCredential(
                credentialId,
                dto.getUsername(),
                hashedPassword,
                dto.getRole()
        );

        final UserCredential saved = userCredentialRepository.save(credential);
        LOGGER.info("Registered user '{}' with role '{}' and id '{}'",
                saved.getUsername(), saved.getRole(), saved.getId());

        final String token = jwtTokenProvider.generateToken(saved);
        return new AuthResponseDTO(token, saved.getId(), saved.getUsername(), saved.getRole().name());
    }

    /**
     * Authenticates existing user credentials and returns a signed JWT token on success.
     *
     * @param dto the login credentials
     * @return AuthResponseDTO containing JWT token and user info
     * @throws AuthenticationException if username is not found or password does not match
     */
    @Transactional(readOnly = true)
    public AuthResponseDTO login(final LoginRequestDTO dto) {
        final UserCredential user = userCredentialRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> {
                    LOGGER.warn("Login failed: username '{}' not found", dto.getUsername());
                    return new AuthenticationException("Invalid username or password");
                });

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            LOGGER.warn("Login failed: invalid password for username '{}'", dto.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }

        LOGGER.info("User '{}' successfully authenticated", user.getUsername());
        final String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(token, user.getId(), user.getUsername(), user.getRole().name());
    }

    /**
     * Validates a JWT token and returns user details extracted from claims.
     *
     * @param token the JWT string
     * @return AuthResponseDTO containing decoded user claims
     * @throws AuthenticationException if token is missing, invalid or expired
     */
    public AuthResponseDTO validate(final String token) {
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("Invalid or expired JWT token");
        }

        final String username = jwtTokenProvider.extractUsername(token);
        final String role = jwtTokenProvider.extractRole(token);
        final String userIdStr = jwtTokenProvider.extractUserId(token);
        final UUID userId = UUID.fromString(userIdStr);

        return new AuthResponseDTO(token, userId, username, role);
    }
}
