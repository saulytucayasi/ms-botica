package com.example.ms_compras.controller;

import com.example.ms_compras.entity.RecepcionMercancia;
import com.example.ms_compras.enums.EstadoRecepcion;
import com.example.ms_compras.service.RecepcionMercanciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recepciones")
@RequiredArgsConstructor
public class RecepcionMercanciaController {
    
    private final RecepcionMercanciaService recepcionMercanciaService;
    
    @GetMapping
    public ResponseEntity<List<RecepcionMercancia>> listar() {
        List<RecepcionMercancia> recepciones = recepcionMercanciaService.listar();
        return ResponseEntity.ok(recepciones);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RecepcionMercancia> buscarPorId(@PathVariable Long id) {
        Optional<RecepcionMercancia> recepcion = recepcionMercanciaService.buscarPorId(id);
        return recepcion.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/numero/{numeroRecepcion}")
    public ResponseEntity<RecepcionMercancia> buscarPorNumeroRecepcion(@PathVariable String numeroRecepcion) {
        Optional<RecepcionMercancia> recepcion = recepcionMercanciaService.buscarPorNumeroRecepcion(numeroRecepcion);
        return recepcion.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/orden-compra/{ordenCompraId}")
    public ResponseEntity<List<RecepcionMercancia>> buscarPorOrdenCompra(@PathVariable Long ordenCompraId) {
        List<RecepcionMercancia> recepciones = recepcionMercanciaService.buscarPorOrdenCompra(ordenCompraId);
        return ResponseEntity.ok(recepciones);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<RecepcionMercancia>> buscarPorEstado(@PathVariable EstadoRecepcion estado) {
        List<RecepcionMercancia> recepciones = recepcionMercanciaService.buscarPorEstado(estado);
        return ResponseEntity.ok(recepciones);
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<RecepcionMercancia>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<RecepcionMercancia> recepciones = recepcionMercanciaService.buscarPorFecha(fechaInicio, fechaFin);
        return ResponseEntity.ok(recepciones);
    }
    
    @PostMapping
    public ResponseEntity<RecepcionMercancia> crear(@RequestBody RecepcionMercancia recepcionMercancia) {
        try {
            RecepcionMercancia nuevaRecepcion = recepcionMercanciaService.crear(recepcionMercancia);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRecepcion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RecepcionMercancia> actualizar(@PathVariable Long id, @RequestBody RecepcionMercancia recepcionMercancia) {
        try {
            RecepcionMercancia recepcionActualizada = recepcionMercanciaService.actualizar(id, recepcionMercancia);
            return ResponseEntity.ok(recepcionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            recepcionMercanciaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam EstadoRecepcion estado) {
        try {
            recepcionMercanciaService.cambiarEstado(id, estado);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/procesar")
    public ResponseEntity<Void> procesarRecepcion(@PathVariable Long id) {
        try {
            recepcionMercanciaService.procesarRecepcion(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}