package com.wetube.wetube_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI postsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wetube API")
                        .version("1.0.0")
                        .description("API documentation for the Wetube CRUD application"));
    }
}
