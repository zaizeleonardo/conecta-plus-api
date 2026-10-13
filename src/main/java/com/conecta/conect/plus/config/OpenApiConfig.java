package com.conecta.conect.plus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuração da documentação da API (Swagger UI em /swagger-ui.html).
 * Habilita o botão "Authorize" para testar os endpoints protegidos com JWT.
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER = "bearerAuth";

    @Bean
    public OpenAPI conectaPlusOpenApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Conecta+ API")
                        .version("1.0.0")
                        .description("API REST que conecta candidatos e empresas, "
                                + "com diagnóstico de compatibilidade entre "
                                + "competências do candidato e requisitos da vaga."))
                .addSecurityItem(new SecurityRequirement().addList(BEARER))
                .components(new Components()
                        .addSecuritySchemes(BEARER, new SecurityScheme()
                                .name(BEARER)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
