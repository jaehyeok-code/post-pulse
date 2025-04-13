package com.project.user.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Post_Pulse",
        version = "1.0",
        description = "API 설명"
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Server")
    }
)
@Configuration
public class OpenApiConfig {
}
