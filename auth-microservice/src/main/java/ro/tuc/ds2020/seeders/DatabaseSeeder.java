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

/**
 * Seeds initial test accounts (admin and client) into user_credentials table if empty.
 */
@Component
@SuppressWarnings("checkstyle:DesignForExtension")
public class DatabaseSeeder implements CommandLineRunner {

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
                    "admin",
                    passwordEncoder.encode("admin123"),
                    Role.ADMIN
            );
            userCredentialRepository.save(admin);
            LOGGER.info("Seeded administrator account: username 'admin', role 'ADMIN'");

            final UserCredential client = new UserCredential(
                    "client",
                    passwordEncoder.encode("client123"),
                    Role.CLIENT
            );
            userCredentialRepository.save(client);
            LOGGER.info("Seeded client account: username 'client', role 'CLIENT'");
        } else {
            LOGGER.info("user_credentials table already contains {} records. Skipping seed.",
                    userCredentialRepository.count());
        }
    }
}
