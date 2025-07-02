package com.example.ms_ventas.repository;

import com.example.ms_ventas.entity.VentaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaItemRepository extends JpaRepository<VentaItem, Long> {
    
    List<VentaItem> findByVentaId(Long ventaId);
    
    List<VentaItem> findByProductoId(Long productoId);
    
    @Query("SELECT vi FROM VentaItem vi WHERE vi.venta.clienteId = :clienteId")
    List<VentaItem> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT SUM(vi.cantidad) FROM VentaItem vi WHERE vi.productoId = :productoId")
    Integer getTotalCantidadVendidaByProducto(@Param("productoId") Long productoId);
}