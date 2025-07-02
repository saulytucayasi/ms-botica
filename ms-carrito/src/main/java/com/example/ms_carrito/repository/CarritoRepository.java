package com.example.ms_carrito.repository;

import com.example.ms_carrito.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    
    Optional<Carrito> findByClienteIdAndActivoTrue(Long clienteId);
    
    Optional<Carrito> findBySessionIdAndActivoTrue(String sessionId);
    
    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.id = :id AND c.activo = true")
    Optional<Carrito> findByIdWithItems(@Param("id") Long id);
    
    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.clienteId = :clienteId AND c.activo = true")
    Optional<Carrito> findByClienteIdWithItems(@Param("clienteId") Long clienteId);
}