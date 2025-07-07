package com.example.ms_compras.feign;

import com.example.ms_compras.dto.InventarioDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventarioFeignFallback implements InventarioFeign {
    
    @Override
    public InventarioDto consultarStock(Long productoId) {
        log.error("Error al consultar stock para producto {}", productoId);
        return InventarioDto.builder()
                .productoId(productoId)
                .stockActual(0)
                .estado("SERVICIO_NO_DISPONIBLE")
                .build();
    }
    
    @Override
    public InventarioDto actualizarStock(Long productoId, Integer cantidad, String tipoMovimiento) {
        log.error("Error al actualizar stock para producto {} con cantidad {} y tipo {}", 
                 productoId, cantidad, tipoMovimiento);
        return InventarioDto.builder()
                .productoId(productoId)
                .stockActual(0)
                .estado("ERROR_ACTUALIZACION")
                .build();
    }
}