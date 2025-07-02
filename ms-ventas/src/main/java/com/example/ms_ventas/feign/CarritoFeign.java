package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.CarritoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ms-carrito-service", fallback = CarritoFeignFallback.class)
public interface CarritoFeign {
    
    @GetMapping("/api/carrito/cliente/{clienteId}")
    ResponseEntity<CarritoDto> obtenerCarritoPorCliente(@PathVariable Long clienteId);
    
    @GetMapping("/api/carrito/session/{sessionId}")
    ResponseEntity<CarritoDto> obtenerCarritoPorSession(@PathVariable String sessionId);
    
    @PutMapping("/api/carrito/{carritoId}/desactivar")
    ResponseEntity<Void> desactivarCarrito(@PathVariable Long carritoId);
}