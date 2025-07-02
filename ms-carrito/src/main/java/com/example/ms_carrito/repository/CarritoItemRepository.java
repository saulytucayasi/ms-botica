package com.example.ms_carrito.repository;

import com.example.ms_carrito.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    
    @Modifying
    @Query("DELETE FROM CarritoItem ci WHERE ci.carrito.id = :carritoId AND ci.productoId = :productoId")
    void deleteByCarritoIdAndProductoId(@Param("carritoId") Long carritoId, @Param("productoId") Long productoId);
    
    @Modifying
    @Query("DELETE FROM CarritoItem ci WHERE ci.carrito.id = :carritoId")
    void deleteAllByCarritoId(@Param("carritoId") Long carritoId);
}