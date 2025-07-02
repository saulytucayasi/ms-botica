package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.CarritoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ms-carrito", fallback = CarritoFeignFallback.class)
public interface CarritoFeign {
    
    @GetMapping("/api/carrito/cliente/{clienteId}")
    @CircuitBreaker(name = "carrito-service", fallbackMethod = "fallbackCarrito")
    ResponseEntity<CarritoDto> obtenerCarritoPorCliente(@PathVariable Long clienteId);
    
    @PutMapping("/api/carrito/{carritoId}/desactivar")
    @CircuitBreaker(name = "carrito-service", fallbackMethod = "fallbackDesactivar")
    ResponseEntity<Void> desactivarCarrito(@PathVariable Long carritoId);
}