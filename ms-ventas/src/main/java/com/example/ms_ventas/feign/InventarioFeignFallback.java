package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.InventarioDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventarioFeignFallback implements InventarioFeign {
    
    @Override
    public ResponseEntity<InventarioDto> obtenerInventarioPorProducto(Long productoId) {
        log.warn("Fallback ejecutado para obtener inventario del producto: {}", productoId);
        
        InventarioDto inventarioFallback = new InventarioDto();
        inventarioFallback.setId(0L);
        inventarioFallback.setProductoId(productoId);
        inventarioFallback.setCantidad(0);
        inventarioFallback.setStockMinimo(0);
        inventarioFallback.setStockMaximo(0);
        
        return ResponseEntity.ok(inventarioFallback);
    }
    
    @Override
    public ResponseEntity<InventarioDto> reducirInventario(Long productoId, Integer cantidad) {
        log.warn("Fallback ejecutado para reducir inventario del producto: {} cantidad: {}", productoId, cantidad);
        return obtenerInventarioPorProducto(productoId);
    }
    
    @Override
    public ResponseEntity<InventarioDto> restaurarInventario(Long productoId, Integer cantidad) {
        log.warn("Fallback ejecutado para restaurar inventario del producto: {} cantidad: {}", productoId, cantidad);
        return obtenerInventarioPorProducto(productoId);
    }
}