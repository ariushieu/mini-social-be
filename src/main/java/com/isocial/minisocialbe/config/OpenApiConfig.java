package com.isocial.minisocialbe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("https://api.qhieu.dev").description("Production server (HTTPS)"),
                        new Server().url("http://api.qhieu.dev").description("Production server (HTTP)"),
                        new Server().url("http://localhost:8080").description("Local development server")
                ))
                .info(new Info()
                        .title("Mini Social Backend REST API")
                        .version("v1.0.0")
                        .description("Social media backend API with posts, likes, follows, and comments")
                        .contact(new Contact()
                                .name("iSocial Team")
                                .email("contact@isocial.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")));
    }
}
