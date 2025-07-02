package com.example.ms_carrito.repository;

import com.example.ms_carrito.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    
    List<CarritoItem> findByCarritoId(Long carritoId);
    
    Optional<CarritoItem> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
    
    @Query("SELECT ci FROM CarritoItem ci WHERE ci.carrito.id = :carritoId AND ci.productoId = :productoId")
    Optional<CarritoItem> findItemByCarritoAndProducto(@Param("carritoId") Long carritoId, @Param("productoId") Long productoId);
    
    void deleteByCarritoIdAndProductoId(Long carritoId, Long productoId);
}