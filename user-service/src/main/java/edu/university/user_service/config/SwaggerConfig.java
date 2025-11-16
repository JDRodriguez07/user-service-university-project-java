package edu.university.user_service.config;

// Importaciones necesarias para la configuración de Swagger
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Marca esta clase como una clase de configuración de Spring
@Configuration
public class SwaggerConfig {

    // Define un bean que configura la documentación OpenAPI
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Configura la información básica de la API
                .info(new Info()
                        .title("API de Gestión de Usuarios para Plataforma Universitaria") // Título de la API
                        .version("1.0") // Versión de la API
                        .description("API para la gestión de usuarios y notas en plataforma universitaria"));
    }
}