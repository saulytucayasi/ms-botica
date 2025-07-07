package com.example.ms_compras.controller;

import com.example.ms_compras.entity.Proveedor;
import com.example.ms_compras.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {
    
    private final ProveedorService proveedorService;
    
    @GetMapping
    public ResponseEntity<List<Proveedor>> listar() {
        List<Proveedor> proveedores = proveedorService.listar();
        return ResponseEntity.ok(proveedores);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorService.buscarPorId(id);
        return proveedor.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Proveedor> buscarPorRuc(@PathVariable String ruc) {
        Optional<Proveedor> proveedor = proveedorService.buscarPorRuc(ruc);
        return proveedor.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Proveedor>> buscarPorNombre(@RequestParam String nombre) {
        List<Proveedor> proveedores = proveedorService.buscarPorNombre(nombre);
        return ResponseEntity.ok(proveedores);
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Proveedor>> listarActivos() {
        List<Proveedor> proveedores = proveedorService.listarActivos();
        return ResponseEntity.ok(proveedores);
    }
    
    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        try {
            Proveedor nuevoProveedor = proveedorService.guardar(proveedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProveedor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        try {
            Proveedor proveedorActualizado = proveedorService.actualizar(id, proveedor);
            return ResponseEntity.ok(proveedorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            proveedorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            proveedorService.cambiarEstado(id, estado);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}