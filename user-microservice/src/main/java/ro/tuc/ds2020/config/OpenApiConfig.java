package ro.tuc.ds2020.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration for the User Management microservice.
 * Exposes a Bearer JWT security scheme so protected routes can be tested directly in Swagger UI.
 */
@Configuration
@SuppressWarnings("checkstyle:DesignForExtension")
public class OpenApiConfig {

    /**
     * Builds the custom OpenAPI descriptor with service metadata and a Bearer-JWT security scheme.
     *
     * @return configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management Microservice API")
                        .version("1.0")
                        .description("Microserviciu de gestionare a utilizatorilor (persoanelor)."
                                + " Ofera operatii CRUD pe entitatea Person din baza de date user_db."))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introdu token-ul JWT obtinut de la auth-service")));
    }
}
