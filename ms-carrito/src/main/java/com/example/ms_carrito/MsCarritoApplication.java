package com.example.ms_carrito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCarritoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCarritoApplication.class, args);
	}

}