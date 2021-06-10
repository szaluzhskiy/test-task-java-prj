package com.example.datstestprj.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "TEST TASK JAVA REST API"
        ),
        servers = {@Server(url = "http://127.0.0.1:8085")}
)
public class OpenApiConfiguration {
}
