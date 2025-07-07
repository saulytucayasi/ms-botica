package com.example.ms_compras.service;

import com.example.ms_compras.entity.OrdenCompra;
import com.example.ms_compras.enums.EstadoOrden;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdenCompraService {
    
    List<OrdenCompra> listar();
    
    Optional<OrdenCompra> buscarPorId(Long id);
    
    Optional<OrdenCompra> buscarPorNumeroOrden(String numeroOrden);
    
    List<OrdenCompra> buscarPorProveedor(Long proveedorId);
    
    List<OrdenCompra> buscarPorEstado(EstadoOrden estado);
    
    List<OrdenCompra> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<OrdenCompra> buscarOrdenesVencidas();
    
    OrdenCompra crear(OrdenCompra ordenCompra);
    
    OrdenCompra actualizar(Long id, OrdenCompra ordenCompra);
    
    void cambiarEstado(Long id, EstadoOrden estado);
    
    void eliminar(Long id);
    
    String generarNumeroOrden();
}