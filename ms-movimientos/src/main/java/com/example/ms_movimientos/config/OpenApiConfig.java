package com.example.ms_movimientos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI movimientosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Movimientos API")
                        .description("Microservicio para gestión de movimientos de inventario")
                        .version("1.0.0"));
    }
}