package com.example.ms_inventario.repository;

import com.example.ms_inventario.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    
    Optional<Inventario> findByProductoId(Long productoId);
    
    @Query("SELECT i FROM Inventario i WHERE i.stockActual <= i.stockMinimo")
    List<Inventario> findProductosConStockBajo();
    
    @Query("SELECT i FROM Inventario i WHERE i.estado = :estado")
    List<Inventario> findByEstado(@Param("estado") String estado);
    
    @Query("SELECT i FROM Inventario i WHERE i.ubicacion = :ubicacion")
    List<Inventario> findByUbicacion(@Param("ubicacion") String ubicacion);
}