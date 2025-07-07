package com.example.ms_compras.controller;

import com.example.ms_compras.entity.OrdenCompra;
import com.example.ms_compras.enums.EstadoOrden;
import com.example.ms_compras.service.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes-compra")
@RequiredArgsConstructor
public class OrdenCompraController {
    
    private final OrdenCompraService ordenCompraService;
    
    @GetMapping
    public ResponseEntity<List<OrdenCompra>> listar() {
        List<OrdenCompra> ordenes = ordenCompraService.listar();
        return ResponseEntity.ok(ordenes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> buscarPorId(@PathVariable Long id) {
        Optional<OrdenCompra> orden = ordenCompraService.buscarPorId(id);
        return orden.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/numero/{numeroOrden}")
    public ResponseEntity<OrdenCompra> buscarPorNumeroOrden(@PathVariable String numeroOrden) {
        Optional<OrdenCompra> orden = ordenCompraService.buscarPorNumeroOrden(numeroOrden);
        return orden.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<OrdenCompra>> buscarPorProveedor(@PathVariable Long proveedorId) {
        List<OrdenCompra> ordenes = ordenCompraService.buscarPorProveedor(proveedorId);
        return ResponseEntity.ok(ordenes);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenCompra>> buscarPorEstado(@PathVariable EstadoOrden estado) {
        List<OrdenCompra> ordenes = ordenCompraService.buscarPorEstado(estado);
        return ResponseEntity.ok(ordenes);
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<OrdenCompra>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<OrdenCompra> ordenes = ordenCompraService.buscarPorFecha(fechaInicio, fechaFin);
        return ResponseEntity.ok(ordenes);
    }
    
    @GetMapping("/vencidas")
    public ResponseEntity<List<OrdenCompra>> buscarOrdenesVencidas() {
        List<OrdenCompra> ordenes = ordenCompraService.buscarOrdenesVencidas();
        return ResponseEntity.ok(ordenes);
    }
    
    @PostMapping
    public ResponseEntity<OrdenCompra> crear(@RequestBody OrdenCompra ordenCompra) {
        try {
            OrdenCompra nuevaOrden = ordenCompraService.crear(ordenCompra);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompra> actualizar(@PathVariable Long id, @RequestBody OrdenCompra ordenCompra) {
        try {
            OrdenCompra ordenActualizada = ordenCompraService.actualizar(id, ordenCompra);
            return ResponseEntity.ok(ordenActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            ordenCompraService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam EstadoOrden estado) {
        try {
            ordenCompraService.cambiarEstado(id, estado);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}