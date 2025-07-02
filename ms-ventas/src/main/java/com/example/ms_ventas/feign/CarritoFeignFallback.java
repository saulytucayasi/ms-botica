package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.CarritoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@Slf4j
public class CarritoFeignFallback implements CarritoFeign {
    
    @Override
    public ResponseEntity<CarritoDto> obtenerCarritoPorCliente(Long clienteId) {
        log.warn("Fallback ejecutado para obtener carrito del cliente: {}", clienteId);
        
        CarritoDto carritoFallback = new CarritoDto();
        carritoFallback.setId(0L);
        carritoFallback.setClienteId(clienteId);
        carritoFallback.setFechaCreacion(LocalDateTime.now());
        carritoFallback.setTotal(BigDecimal.ZERO);
        carritoFallback.setActivo(false);
        carritoFallback.setItems(new ArrayList<>());
        
        return ResponseEntity.ok(carritoFallback);
    }
    
    @Override
    public ResponseEntity<Void> desactivarCarrito(Long carritoId) {
        log.warn("Fallback ejecutado para desactivar carrito: {}", carritoId);
        return ResponseEntity.ok().build();
    }
}