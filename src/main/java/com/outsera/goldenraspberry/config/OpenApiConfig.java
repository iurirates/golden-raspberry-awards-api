package com.outsera.goldenraspberry.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI goldenRaspberryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Golden Raspberry Awards API")
                        .description("API RESTful para leitura de indicados e vencedores da categoria "
                                + "Pior Filme do Golden Raspberry Awards.")
                        .version("1.0.0")
                        .license(new License().name("MIT")));
    }
}
