package com.example.ms_inventario.controller;

import com.example.ms_inventario.entity.Inventario;
import com.example.ms_inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {
    
    private final InventarioService inventarioService;
    
    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> buscarPorId(@PathVariable Long id) {
        Optional<Inventario> inventario = inventarioService.buscarPorId(id);
        return inventario.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Inventario> buscarPorProductoId(@PathVariable Long productoId) {
        Optional<Inventario> inventario = inventarioService.buscarPorProductoId(productoId);
        return inventario.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario inventario) {
        return ResponseEntity.ok(inventarioService.guardar(inventario));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable Long id, @RequestBody Inventario inventario) {
        Optional<Inventario> inventarioExistente = inventarioService.buscarPorId(id);
        if (inventarioExistente.isPresent()) {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioService.guardar(inventario));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/actualizar-stock")
    public ResponseEntity<Inventario> actualizarStock(@RequestBody Map<String, Object> request) {
        Long productoId = Long.valueOf(request.get("productoId").toString());
        Integer cantidad = Integer.valueOf(request.get("cantidad").toString());
        String tipoMovimiento = request.get("tipoMovimiento").toString();
        
        try {
            Inventario inventarioActualizado = inventarioService.actualizarStock(productoId, cantidad, tipoMovimiento);
            return ResponseEntity.ok(inventarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Inventario>> obtenerProductosConStockBajo() {
        return ResponseEntity.ok(inventarioService.obtenerProductosConStockBajo());
    }
    
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<Inventario>> obtenerPorUbicacion(@PathVariable String ubicacion) {
        return ResponseEntity.ok(inventarioService.obtenerPorUbicacion(ubicacion));
    }
}