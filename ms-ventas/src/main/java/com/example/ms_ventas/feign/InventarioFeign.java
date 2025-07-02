package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.InventarioDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "ms-inventario-service", fallback = InventarioFeignFallback.class)
public interface InventarioFeign {
    
    @GetMapping("/inventario/producto/{productoId}")
    ResponseEntity<InventarioDto> obtenerInventarioPorProducto(@PathVariable Long productoId);
    
    @PutMapping("/inventario/actualizar-stock")
    ResponseEntity<InventarioDto> actualizarStock(@RequestBody Map<String, Object> request);
}