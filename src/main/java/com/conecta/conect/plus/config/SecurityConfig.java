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

                // Cadastro e login são públicos
                .requestMatchers(
                    "/usuarios",
                    "/usuarios/login"
                ).permitAll()

                // Visualização de vagas
                // Qualquer usuário autenticado pode visualizar
                .requestMatchers(
                    HttpMethod.GET,
                    "/vagas/**"
                ).authenticated()

                // Criar vaga: somente ADMIN
                .requestMatchers(
                    HttpMethod.POST,
                    "/vagas/**"
                ).hasRole("ADMIN")

                // Editar vaga: somente ADMIN
                .requestMatchers(
                    HttpMethod.PUT,
                    "/vagas/**"
                ).hasRole("ADMIN")

                // Excluir vaga: somente ADMIN
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/vagas/**"
                ).hasRole("ADMIN")

                // Demais endpoints exigem autenticação
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