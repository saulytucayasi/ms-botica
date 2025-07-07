package com.example.ms_compras.repository;

import com.example.ms_compras.entity.OrdenCompra;
import com.example.ms_compras.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    
    Optional<OrdenCompra> findByNumeroOrden(String numeroOrden);
    
    List<OrdenCompra> findByProveedorId(Long proveedorId);
    
    List<OrdenCompra> findByEstado(EstadoOrden estado);
    
    @Query("SELECT o FROM OrdenCompra o WHERE o.fechaOrden BETWEEN :fechaInicio AND :fechaFin")
    List<OrdenCompra> findByFechaOrdenBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT o FROM OrdenCompra o WHERE o.proveedorId = :proveedorId AND o.estado = :estado")
    List<OrdenCompra> findByProveedorIdAndEstado(Long proveedorId, EstadoOrden estado);
    
    @Query("SELECT o FROM OrdenCompra o WHERE o.fechaEntregaEsperada < :fecha AND o.estado IN ('PENDIENTE', 'ENVIADA')")
    List<OrdenCompra> findOrdenesVencidas(LocalDateTime fecha);
}