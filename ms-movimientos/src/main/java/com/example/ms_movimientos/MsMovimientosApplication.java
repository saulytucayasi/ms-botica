package com.example.ms_movimientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsMovimientosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsMovimientosApplication.class, args);
    }

}