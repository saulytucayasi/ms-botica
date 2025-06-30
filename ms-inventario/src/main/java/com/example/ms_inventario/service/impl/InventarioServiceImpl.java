package com.example.ms_inventario.service.impl;

import com.example.ms_inventario.entity.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;
import com.example.ms_inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {
    
    private final InventarioRepository inventarioRepository;
    
    @Override
    public List<Inventario> listar() {
        return inventarioRepository.findAll();
    }
    
    @Override
    public Optional<Inventario> buscarPorId(Long id) {
        return inventarioRepository.findById(id);
    }
    
    @Override
    public Optional<Inventario> buscarPorProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }
    
    @Override
    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }
    
    @Override
    public void eliminar(Long id) {
        inventarioRepository.deleteById(id);
    }
    
    @Override
    public Inventario actualizarStock(Long productoId, Integer cantidad, String tipoMovimiento) {
        Optional<Inventario> inventarioOpt = inventarioRepository.findByProductoId(productoId);
        
        if (inventarioOpt.isPresent()) {
            Inventario inventario = inventarioOpt.get();
            
            if ("ENTRADA".equals(tipoMovimiento)) {
                inventario.setStockActual(inventario.getStockActual() + cantidad);
            } else if ("SALIDA".equals(tipoMovimiento)) {
                int nuevoStock = inventario.getStockActual() - cantidad;
                if (nuevoStock < 0) {
                    throw new RuntimeException("Stock insuficiente");
                }
                inventario.setStockActual(nuevoStock);
            }
            
            return inventarioRepository.save(inventario);
        } else {
            throw new RuntimeException("Producto no encontrado en inventario");
        }
    }
    
    @Override
    public List<Inventario> obtenerProductosConStockBajo() {
        return inventarioRepository.findProductosConStockBajo();
    }
    
    @Override
    public List<Inventario> obtenerPorUbicacion(String ubicacion) {
        return inventarioRepository.findByUbicacion(ubicacion);
    }
}