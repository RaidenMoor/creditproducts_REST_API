package com.example.creditproducts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    //http://localhost:8080/api/swagger-ui/index.html
    @Bean
    public OpenAPI TicketOfficeProject() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API для управления кредитными продуктами")
                        .description("Сервис, позволяющий управлять кредитными продуктами банковского приложения.")
                        .version("v0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                        .contact(new Contact().name("Maria")
                                .email("")
                                .url(""))
                );
    }
}