package com.example.ms_movimientos.service.impl;

import com.example.ms_movimientos.entity.Movimiento;
import com.example.ms_movimientos.feign.InventarioFeign;
import com.example.ms_movimientos.repository.MovimientoRepository;
import com.example.ms_movimientos.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {
    
    private final MovimientoRepository movimientoRepository;
    private final InventarioFeign inventarioFeign;
    
    @Override
    public List<Movimiento> listar() {
        return movimientoRepository.findAll();
    }
    
    @Override
    public Optional<Movimiento> buscarPorId(Long id) {
        return movimientoRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Movimiento registrarMovimiento(Movimiento movimiento) {
        // Calcular costo total si se proporciona precio unitario
        if (movimiento.getPrecioUnitario() != null && movimiento.getCantidad() != null) {
            BigDecimal costoTotal = movimiento.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(movimiento.getCantidad()));
            movimiento.setCostoTotal(costoTotal);
        }
        
        // Guardar el movimiento
        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        
        // Actualizar el inventario a través del microservicio de inventario
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("productoId", movimiento.getProductoId());
            request.put("cantidad", movimiento.getCantidad());
            request.put("tipoMovimiento", movimiento.getTipoMovimiento());
            
            inventarioFeign.actualizarStock(request);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar inventario: " + e.getMessage());
        }
        
        return movimientoGuardado;
    }
    
    @Override
    public List<Movimiento> obtenerMovimientosPorProducto(Long productoId) {
        return movimientoRepository.findByProductoIdOrderByFechaMovimientoDesc(productoId);
    }
    
    @Override
    public List<Movimiento> obtenerMovimientosPorTipo(String tipoMovimiento) {
        return movimientoRepository.findByTipoMovimiento(tipoMovimiento);
    }
    
    @Override
    public List<Movimiento> obtenerMovimientosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
    
    @Override
    public List<Movimiento> obtenerMovimientosPorProductoYFecha(Long productoId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByProductoIdAndFechaBetween(productoId, fechaInicio, fechaFin);
    }
    
    @Override
    public List<Movimiento> obtenerMovimientosPorUsuario(String usuario) {
        return movimientoRepository.findByUsuario(usuario);
    }
}