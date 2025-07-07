package com.example.ms_compras.repository;

import com.example.ms_compras.entity.RecepcionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecepcionItemRepository extends JpaRepository<RecepcionItem, Long> {
    
    List<RecepcionItem> findByRecepcionMercanciaId(Long recepcionMercanciaId);
    
    List<RecepcionItem> findByProductoId(Long productoId);
    
    @Query("SELECT ri FROM RecepcionItem ri WHERE ri.recepcionMercancia.id = :recepcionMercanciaId")
    List<RecepcionItem> findItemsByRecepcionMercanciaId(Long recepcionMercanciaId);
    
    @Query("SELECT ri FROM RecepcionItem ri WHERE ri.productoId = :productoId AND ri.cantidadRecibida > 0")
    List<RecepcionItem> findItemsRecibidosByProductoId(Long productoId);
}