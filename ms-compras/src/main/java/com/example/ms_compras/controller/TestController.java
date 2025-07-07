package com.example.ms_compras.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("MS-Compras está funcionando correctamente!");
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is UP - Port: " + System.getProperty("server.port"));
    }
}