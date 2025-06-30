package com.example.ms_inventario.service;

import com.example.ms_inventario.entity.Inventario;

import java.util.List;
import java.util.Optional;

public interface InventarioService {
    
    List<Inventario> listar();
    
    Optional<Inventario> buscarPorId(Long id);
    
    Optional<Inventario> buscarPorProductoId(Long productoId);
    
    Inventario guardar(Inventario inventario);
    
    void eliminar(Long id);
    
    Inventario actualizarStock(Long productoId, Integer cantidad, String tipoMovimiento);
    
    List<Inventario> obtenerProductosConStockBajo();
    
    List<Inventario> obtenerPorUbicacion(String ubicacion);
}