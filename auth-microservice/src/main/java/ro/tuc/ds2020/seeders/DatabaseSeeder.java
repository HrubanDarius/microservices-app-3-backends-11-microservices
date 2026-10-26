package ro.tuc.ds2020.seeders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.entities.Role;
import ro.tuc.ds2020.entities.UserCredential;
import ro.tuc.ds2020.repositories.UserCredentialRepository;

import java.util.UUID;

/**
 * Seeds initial test accounts (admin and client) into user_credentials table if empty.
 * Client uses a fixed DEMO_CLIENT_ID to synchronize across all microservices.
 */
@Component
@SuppressWarnings("checkstyle:DesignForExtension")
public class DatabaseSeeder implements CommandLineRunner {

    /** Fixed UUID constant for the demo client shared across auth, user and device services. */
    public static final UUID DEMO_CLIENT_ID = UUID.fromString("c11e7100-0000-0000-0000-000000000001");

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserCredentialRepository userCredentialRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Injects dependencies needed for database seeding.
     *
     * @param userCredentialRepository repository to persist credentials
     * @param passwordEncoder          encoder to hash default passwords
     */
    @Autowired
    public DatabaseSeeder(final UserCredentialRepository userCredentialRepository,
                          final BCryptPasswordEncoder passwordEncoder) {
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(final String... args) {
        if (userCredentialRepository.count() == 0) {
            LOGGER.info("user_credentials table is empty. Seeding default test accounts...");

            final UserCredential admin = new UserCredential(
                    UUID.randomUUID(),
                    "admin",
                    passwordEncoder.encode("admin123"),
                    Role.ADMIN
            );
            userCredentialRepository.save(admin);
            LOGGER.info("Seeded administrator account: username 'admin', role 'ADMIN'");

            final UserCredential client = new UserCredential(
                    DEMO_CLIENT_ID,
                    "client",
                    passwordEncoder.encode("client123"),
                    Role.CLIENT
            );
            userCredentialRepository.save(client);
            LOGGER.info("Seeded client account: username 'client', role 'CLIENT' with ID {}", DEMO_CLIENT_ID);
        } else {
            LOGGER.info("user_credentials table already contains {} records. Skipping seed.",
                    userCredentialRepository.count());
        }
    }
}
