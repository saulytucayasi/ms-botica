package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.InventarioDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-inventario", fallback = InventarioFeignFallback.class)
public interface InventarioFeign {
    
    @GetMapping("/api/inventario/producto/{productoId}")
    @CircuitBreaker(name = "inventario-service", fallbackMethod = "fallbackInventario")
    ResponseEntity<InventarioDto> obtenerInventarioPorProducto(@PathVariable Long productoId);
    
    @PutMapping("/api/inventario/producto/{productoId}/reducir")
    @CircuitBreaker(name = "inventario-service", fallbackMethod = "fallbackReducir")
    ResponseEntity<InventarioDto> reducirInventario(@PathVariable Long productoId, @RequestParam Integer cantidad);
    
    @PutMapping("/api/inventario/producto/{productoId}/restaurar")
    @CircuitBreaker(name = "inventario-service", fallbackMethod = "fallbackRestaurar")
    ResponseEntity<InventarioDto> restaurarInventario(@PathVariable Long productoId, @RequestParam Integer cantidad);
}