package com.publicissapient.cryptohistory.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Crypto History API",
                version = "1.0.0",
                description = "This is a Crypto History API documentation"
        )
)
public class SwaggerConfig {
}
