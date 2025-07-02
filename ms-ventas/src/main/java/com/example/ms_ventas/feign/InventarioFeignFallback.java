package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.InventarioDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class InventarioFeignFallback implements InventarioFeign {
    
    @Override
    public ResponseEntity<InventarioDto> obtenerInventarioPorProducto(Long productoId) {
        log.warn("Fallback ejecutado para obtener inventario del producto: {}", productoId);
        
        InventarioDto inventarioFallback = new InventarioDto();
        inventarioFallback.setId(0L);
        inventarioFallback.setProductoId(productoId);
        inventarioFallback.setStockActual(0);
        inventarioFallback.setStockMinimo(0);
        inventarioFallback.setStockMaximo(0);
        
        return ResponseEntity.ok(inventarioFallback);
    }
    
    @Override
    public ResponseEntity<InventarioDto> actualizarStock(Map<String, Object> request) {
        Long productoId = Long.valueOf(request.get("productoId").toString());
        Integer cantidad = Integer.valueOf(request.get("cantidad").toString());
        String tipoMovimiento = request.get("tipoMovimiento").toString();
        
        log.warn("Fallback ejecutado para actualizar stock del producto: {} cantidad: {} tipo: {}", 
                productoId, cantidad, tipoMovimiento);
        
        return obtenerInventarioPorProducto(productoId);
    }
}