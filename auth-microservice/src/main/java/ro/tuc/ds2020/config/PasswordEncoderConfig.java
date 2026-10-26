package ro.tuc.ds2020.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Spring configuration class that exposes a {@link BCryptPasswordEncoder} bean
 * for use throughout the application (password hashing on register,
 * password verification on login).
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Creates and registers a BCryptPasswordEncoder with the default strength (10 rounds).
     *
     * @return a configured BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
