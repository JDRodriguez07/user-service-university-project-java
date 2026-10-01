package edu.university.user_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ===== Endpoints públicos =====
                        .requestMatchers(
                                "/auth/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()

                        // ===== Solo ADMIN puede CREAR / EDITAR / ELIMINAR =====
                        .requestMatchers(HttpMethod.POST, "/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/students/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/teachers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/teachers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/teachers/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/administrators/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/administrators/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/administrators/**").hasRole("ADMIN")

                        // ===== Perfil propio: ADMIN, STUDENT y TEACHER pueden editar sus datos básicos =====
                        .requestMatchers(HttpMethod.PUT, "/users/me/profile")
                        .hasAnyRole("ADMIN", "STUDENT", "TEACHER")

                        // ===== Lecturas (GET) solo autenticado (cualquier rol) =====
                        .requestMatchers(HttpMethod.GET, "/students/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/teachers/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/administrators/**").authenticated()

                        // ===== Todo lo demás requiere estar autenticado =====
                        .anyRequest().authenticated())

                // Registrar tu filtro JWT antes del filtro de username/password
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationManager (para usar en UserService.login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // PasswordEncoder global
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
