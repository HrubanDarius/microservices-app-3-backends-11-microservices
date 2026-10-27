package ro.tuc.ds2020.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.tuc.ds2020.dtos.AuthResponseDTO;
import ro.tuc.ds2020.dtos.LoginRequestDTO;
import ro.tuc.ds2020.dtos.RegisterRequestDTO;
import ro.tuc.ds2020.services.AuthService;

/**
 * REST controller exposing endpoints for user registration, login, and token validation.
 * Base path: /auth (accessible via /auth-service/auth when servlet context-path is active).
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/auth")
@Tag(name = "Authentication", description = "Operatii de autentificare: Register, Login si validare JWT") //pt swagger
@SuppressWarnings("checkstyle:DesignForExtension")
public class AuthController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;

    /**
     * Injects the authentication service.
     *
     * @param authService the auth service
     */
    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles new user registration.
     *
     * @param registerDTO registration payload with username, password and role
     * @return response with issued JWT token and user info (HTTP 201 Created)
     */
    @Operation(summary = "Inregistreaza un utilizator nou",
            description = "Creeaza un cont nou in baza de date si returneaza un token JWT semnat.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Utilizator inregistrat cu succes — token JWT returnat"),
        @ApiResponse(responseCode = "400", description = "Date invalide in request body"),
        @ApiResponse(responseCode = "409", description = "Username deja existent")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody final RegisterRequestDTO registerDTO) {
        final AuthResponseDTO response = authService.register(registerDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Authenticates credentials and returns a signed JWT token.
     *
     * @param loginDTO credentials payload with username and password
     * @return response with issued JWT token and user info (HTTP 200 OK)
     */
    @Operation(summary = "Autentificare (Login)",
            description = "Verifica credentialele si returneaza un token JWT semnat cu rol si userId.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autentificare reusita — token JWT returnat"),
        @ApiResponse(responseCode = "400", description = "Date invalide in request body"),
        @ApiResponse(responseCode = "401", description = "Credentiale invalide")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody final LoginRequestDTO loginDTO) {
        final AuthResponseDTO response = authService.login(loginDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Validates a JWT token provided in the Authorization header or as a query param.
     *
     * @param authHeader optional Authorization header (Bearer &lt;token&gt;)
     * @param tokenParam optional token query parameter fallback
     * @return response with decoded user info if token is valid (HTTP 200 OK)
     */
    @Operation(summary = "Valideaza un token JWT",
            description = "Verifica semnatura si expirarea token-ului JWT si returneaza datele utilizatorului.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token valid — date utilizator returnate"),
        @ApiResponse(responseCode = "401", description = "Token invalid sau expirat")
    })
    @GetMapping(value = "/validate")
    public ResponseEntity<AuthResponseDTO> validate(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            final String authHeader,
            @RequestParam(value = "token", required = false)
            final String tokenParam) {
        String token = null;
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            token = authHeader.substring(BEARER_PREFIX.length()).trim();
        } else if (tokenParam != null && !tokenParam.isBlank()) {
            token = tokenParam.trim();
        }

        final AuthResponseDTO response = authService.validate(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
