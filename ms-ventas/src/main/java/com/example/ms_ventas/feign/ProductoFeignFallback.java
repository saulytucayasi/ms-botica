package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.ProductoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class ProductoFeignFallback implements ProductoFeign {
    
    @Override
    public ResponseEntity<ProductoDto> obtenerProducto(Integer id) {
        log.warn("Fallback ejecutado para obtener producto con ID: {}", id);
        
        ProductoDto productoFallback = new ProductoDto();
        productoFallback.setId(id);
        productoFallback.setNombre("Producto no disponible");
        productoFallback.setDescripcion("Servicio temporalmente no disponible");
        productoFallback.setPrecio(BigDecimal.ZERO);
        productoFallback.setCategoriaId(0);
        
        return ResponseEntity.ok(productoFallback);
    }
}