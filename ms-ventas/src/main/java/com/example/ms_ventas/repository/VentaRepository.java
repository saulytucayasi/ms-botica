package com.example.ms_ventas.repository;

import com.example.ms_ventas.entity.Venta;
import com.example.ms_ventas.enums.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    Optional<Venta> findByNumeroVenta(String numeroVenta);
    
    List<Venta> findByClienteIdOrderByFechaVentaDesc(Long clienteId);
    
    List<Venta> findAllByOrderByFechaVentaDesc();
    
    List<Venta> findByEstado(EstadoVenta estado);
    
    Page<Venta> findByClienteId(Long clienteId, Pageable pageable);
    
    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.items WHERE v.id = :id")
    Optional<Venta> findByIdWithItems(@Param("id") Long id);
    
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findByFechaVentaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                        @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT v FROM Venta v WHERE v.clienteId = :clienteId AND v.estado = :estado")
    List<Venta> findByClienteIdAndEstado(@Param("clienteId") Long clienteId, 
                                         @Param("estado") EstadoVenta estado);
}