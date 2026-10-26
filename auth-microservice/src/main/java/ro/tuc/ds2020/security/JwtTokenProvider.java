package ro.tuc.ds2020.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.entities.UserCredential;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Component responsible for generating and validating JWT tokens.
 * Reads configuration from application.properties (jwt.secret, jwt.expiration).
 */
@Component
@SuppressWarnings("checkstyle:DesignForExtension")
public class JwtTokenProvider {

    /** The secret key used to sign tokens (loaded from application.properties). */
    private final String secret;

    /** Token validity in milliseconds (loaded from application.properties). */
    private final long expiration;

    /**
     * Constructs the provider by injecting jwt properties.
     *
     * @param secret     the HMAC secret key string
     * @param expiration the token lifetime in milliseconds
     */
    public JwtTokenProvider(
            @Value("${jwt.secret}") final String secret,
            @Value("${jwt.expiration}") final long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * Builds the HMAC-SHA256 signing key from the configured secret string.
     *
     * @return a {@link SecretKey} for HMAC-SHA256 signing
     */
    private SecretKey signingKey() {
        final byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a signed JWT token for the given user credential.
     * The token payload contains: sub (username), userId, role, iat, exp.
     *
     * @param user the authenticated or newly registered user
     * @return a compact, signed JWT string
     */
    public String generateToken(final UserCredential user) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId().toString())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey())
                .compact();
    }

    /**
     * Parses and validates the given JWT token string.
     * Returns true if the token is well-formed, signed correctly, and not expired.
     *
     * @param token the JWT string to validate
     * @return true if valid, false otherwise
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Extracts all claims from a validated JWT token.
     *
     * @param token the JWT string
     * @return the Claims payload
     */
    public Claims extractClaims(final String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT string
     * @return the username
     */
    public String extractUsername(final String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts the role claim from a JWT token.
     *
     * @param token the JWT string
     * @return the role name (e.g. "ADMIN" or "CLIENT")
     */
    public String extractRole(final String token) {
        return extractClaims(token).get("role", String.class);
    }

    /**
     * Extracts the userId claim from a JWT token.
     *
     * @param token the JWT string
     * @return the userId as a UUID string
     */
    public String extractUserId(final String token) {
        return extractClaims(token).get("userId", String.class);
    }
}
