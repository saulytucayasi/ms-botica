package com.example.ms_compras.repository;

import com.example.ms_compras.entity.RecepcionMercancia;
import com.example.ms_compras.enums.EstadoRecepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecepcionMercanciaRepository extends JpaRepository<RecepcionMercancia, Long> {
    
    Optional<RecepcionMercancia> findByNumeroRecepcion(String numeroRecepcion);
    
    List<RecepcionMercancia> findByOrdenCompraId(Long ordenCompraId);
    
    List<RecepcionMercancia> findByEstado(EstadoRecepcion estado);
    
    @Query("SELECT r FROM RecepcionMercancia r WHERE r.fechaRecepcion BETWEEN :fechaInicio AND :fechaFin")
    List<RecepcionMercancia> findByFechaRecepcionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT r FROM RecepcionMercancia r WHERE r.ordenCompraId = :ordenCompraId AND r.estado = :estado")
    List<RecepcionMercancia> findByOrdenCompraIdAndEstado(Long ordenCompraId, EstadoRecepcion estado);
}