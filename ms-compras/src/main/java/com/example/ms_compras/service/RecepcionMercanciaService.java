package com.example.ms_compras.service;

import com.example.ms_compras.entity.RecepcionMercancia;
import com.example.ms_compras.enums.EstadoRecepcion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecepcionMercanciaService {
    
    List<RecepcionMercancia> listar();
    
    Optional<RecepcionMercancia> buscarPorId(Long id);
    
    Optional<RecepcionMercancia> buscarPorNumeroRecepcion(String numeroRecepcion);
    
    List<RecepcionMercancia> buscarPorOrdenCompra(Long ordenCompraId);
    
    List<RecepcionMercancia> buscarPorEstado(EstadoRecepcion estado);
    
    List<RecepcionMercancia> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    RecepcionMercancia crear(RecepcionMercancia recepcionMercancia);
    
    RecepcionMercancia actualizar(Long id, RecepcionMercancia recepcionMercancia);
    
    void cambiarEstado(Long id, EstadoRecepcion estado);
    
    void eliminar(Long id);
    
    String generarNumeroRecepcion();
    
    void procesarRecepcion(Long recepcionId);
}