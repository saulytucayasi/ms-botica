package com.example.ms_compras.repository;

import com.example.ms_compras.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    List<Proveedor> findByEstado(String estado);
    
    Optional<Proveedor> findByRuc(String ruc);
    
    @Query("SELECT p FROM Proveedor p WHERE p.nombre LIKE %:nombre%")
    List<Proveedor> buscarPorNombre(String nombre);
    
    @Query("SELECT p FROM Proveedor p WHERE p.estado = 'ACTIVO'")
    List<Proveedor> findProveedoresActivos();
}