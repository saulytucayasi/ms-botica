package com.example.ms_movimientos.service;

import com.example.ms_movimientos.entity.Movimiento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoService {
    
    List<Movimiento> listar();
    
    Optional<Movimiento> buscarPorId(Long id);
    
    Movimiento registrarMovimiento(Movimiento movimiento);
    
    List<Movimiento> obtenerMovimientosPorProducto(Long productoId);
    
    List<Movimiento> obtenerMovimientosPorTipo(String tipoMovimiento);
    
    List<Movimiento> obtenerMovimientosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Movimiento> obtenerMovimientosPorProductoYFecha(Long productoId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Movimiento> obtenerMovimientosPorUsuario(String usuario);
}