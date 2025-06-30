package com.example.ms_movimientos.repository;

import com.example.ms_movimientos.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    
    List<Movimiento> findByProductoIdOrderByFechaMovimientoDesc(Long productoId);
    
    List<Movimiento> findByTipoMovimiento(String tipoMovimiento);
    
    @Query("SELECT m FROM Movimiento m WHERE m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                       @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT m FROM Movimiento m WHERE m.productoId = :productoId AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByProductoIdAndFechaBetween(@Param("productoId") Long productoId,
                                                    @Param("fechaInicio") LocalDateTime fechaInicio,
                                                    @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT m FROM Movimiento m WHERE m.usuario = :usuario ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> findByUsuario(@Param("usuario") String usuario);
}