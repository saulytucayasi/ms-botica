package com.example.ms_compras.feign;

import com.example.ms_compras.dto.InventarioDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "ms-inventario-service", fallback = InventarioFeignFallback.class)
public interface InventarioFeign {
    
    @GetMapping("/inventario/producto/{productoId}")
    @CircuitBreaker(name = "inventarioConsultarStockCB", fallbackMethod = "fallbackConsultarStock")
    InventarioDto consultarStock(@PathVariable Long productoId);
    
    @PutMapping("/inventario/actualizar-stock")
    @CircuitBreaker(name = "inventarioActualizarStockCB", fallbackMethod = "fallbackActualizarStock")
    InventarioDto actualizarStock(@RequestParam Long productoId, @RequestParam Integer cantidad, @RequestParam String tipoMovimiento);
    
    default InventarioDto fallbackConsultarStock(Long productoId, Exception exception) {
        return InventarioDto.builder()
                .productoId(productoId)
                .stockActual(0)
                .estado("DESCONOCIDO")
                .build();
    }
    
    default InventarioDto fallbackActualizarStock(Long productoId, Integer cantidad, String tipoMovimiento, Exception exception) {
        return InventarioDto.builder()
                .productoId(productoId)
                .stockActual(0)
                .estado("ERROR")
                .build();
    }
}