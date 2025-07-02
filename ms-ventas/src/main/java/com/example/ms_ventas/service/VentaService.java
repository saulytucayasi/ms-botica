package com.example.ms_ventas.service;

import com.example.ms_ventas.entity.Venta;
import com.example.ms_ventas.entity.VentaItem;
import com.example.ms_ventas.enums.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {
    
    Venta crearVentaDesdeCarrito(Long clienteId, Long carritoId);
    
    Venta obtenerVentaPorId(Long ventaId);
    
    Optional<Venta> obtenerVentaPorNumero(String numeroVenta);
    
    List<Venta> obtenerVentasPorCliente(Long clienteId);
    
    Page<Venta> obtenerVentasPorClientePaginado(Long clienteId, Pageable pageable);
    
    List<Venta> obtenerVentasPorEstado(EstadoVenta estado);
    
    List<Venta> obtenerVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    Venta actualizarEstadoVenta(Long ventaId, EstadoVenta nuevoEstado);
    
    Venta cancelarVenta(Long ventaId, String motivo);
    
    List<VentaItem> obtenerItemsVenta(Long ventaId);
    
    void procesarVenta(Long ventaId);
}