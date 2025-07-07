package com.example.ms_compras.service;

import com.example.ms_compras.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    
    List<Proveedor> listar();
    
    Optional<Proveedor> buscarPorId(Long id);
    
    Optional<Proveedor> buscarPorRuc(String ruc);
    
    List<Proveedor> buscarPorNombre(String nombre);
    
    List<Proveedor> listarActivos();
    
    Proveedor guardar(Proveedor proveedor);
    
    Proveedor actualizar(Long id, Proveedor proveedor);
    
    void eliminar(Long id);
    
    void cambiarEstado(Long id, String estado);
}