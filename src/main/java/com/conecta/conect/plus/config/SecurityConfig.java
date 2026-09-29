package com.conecta.conect.plus.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors ->
                cors.configurationSource(corsConfigurationSource())
            )

            .authorizeHttpRequests(auth -> auth

                // ====================================================
                // CADASTRO E LOGIN - PÚBLICOS
                // ====================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/usuarios"
                ).permitAll()

                .requestMatchers(
                    "/usuarios/login"
                ).permitAll()

                // ====================================================
                // LISTAR USUÁRIOS - SOMENTE ADMIN
                // ====================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/usuarios"
                ).hasRole("ADMIN")

                // ====================================================
                // VISUALIZAÇÃO DE VAGAS
                // Qualquer usuário autenticado pode visualizar
                // ====================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/vagas/**"
                ).authenticated()

                // ====================================================
                // CRIAR VAGA
                // ADMIN ou EMPRESA
                // ====================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/vagas/**"
                ).hasAnyRole("ADMIN", "EMPRESA")

                // ====================================================
                // EDITAR VAGA
                // ADMIN ou EMPRESA
                // ====================================================

                .requestMatchers(
                    HttpMethod.PUT,
                    "/vagas/**"
                ).hasAnyRole("ADMIN", "EMPRESA")

                // ====================================================
                // EXCLUIR VAGA
                // ADMIN ou EMPRESA
                // ====================================================

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/vagas/**"
                ).hasAnyRole("ADMIN", "EMPRESA")

                // ====================================================
                // DEMAIS ENDPOINTS
                // ====================================================

                .anyRequest().authenticated()
            );

        // Filtro JWT executado antes do filtro padrão
        // do Spring Security
        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
            new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://conecta-plus-front.vercel.app"
        ));

        configuration.setAllowedMethods(List.of(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
}