package com.example.ms_compras.repository;

import com.example.ms_compras.entity.OrdenCompraItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraItemRepository extends JpaRepository<OrdenCompraItem, Long> {
    
    List<OrdenCompraItem> findByOrdenCompraId(Long ordenCompraId);
    
    List<OrdenCompraItem> findByProductoId(Long productoId);
    
    @Query("SELECT oci FROM OrdenCompraItem oci WHERE oci.ordenCompra.id = :ordenCompraId")
    List<OrdenCompraItem> findItemsByOrdenCompraId(Long ordenCompraId);
    
    @Query("SELECT oci FROM OrdenCompraItem oci WHERE oci.productoId = :productoId AND oci.ordenCompra.estado = 'PENDIENTE'")
    List<OrdenCompraItem> findItemsPendientesByProductoId(Long productoId);
}