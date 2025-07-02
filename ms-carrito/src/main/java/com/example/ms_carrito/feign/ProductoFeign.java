package com.example.ms_carrito.feign;

import com.example.ms_carrito.dto.ProductoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-producto", fallback = ProductoFeignFallback.class)
public interface ProductoFeign {
    
    @GetMapping("/api/producto/{id}")
    @CircuitBreaker(name = "producto-service", fallbackMethod = "fallbackProducto")
    ResponseEntity<ProductoDto> obtenerProducto(@PathVariable Integer id);
}