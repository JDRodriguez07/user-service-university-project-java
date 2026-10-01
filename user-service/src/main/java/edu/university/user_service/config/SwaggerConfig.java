package edu.university.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // Crear el esquema de seguridad JWT (Bearer)
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(In.HEADER)
                .name("Authorization");

        // Requisito de seguridad global (toda la API requiere Bearer)
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Usuarios para Plataforma Universitaria")
                        .version("1.0")
                        .description("API para la gestión de usuarios y notas en plataforma universitaria"))
                // Añadir esquema y requirement
                .addSecurityItem(securityRequirement)
                .schemaRequirement("bearerAuth", bearerAuth);
    }
}
