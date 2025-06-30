package com.example.ms_movimientos.controller;

import com.example.ms_movimientos.entity.Movimiento;
import com.example.ms_movimientos.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {
    
    private final MovimientoService movimientoService;
    
    @GetMapping
    public ResponseEntity<List<Movimiento>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> buscarPorId(@PathVariable Long id) {
        Optional<Movimiento> movimiento = movimientoService.buscarPorId(id);
        return movimiento.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Movimiento> registrarMovimiento(@RequestBody Movimiento movimiento) {
        try {
            Movimiento movimientoRegistrado = movimientoService.registrarMovimiento(movimiento);
            return ResponseEntity.ok(movimientoRegistrado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientosPorProducto(productoId));
    }
    
    @GetMapping("/tipo/{tipoMovimiento}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorTipo(@PathVariable String tipoMovimiento) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientosPorTipo(tipoMovimiento));
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientosPorFecha(fechaInicio, fechaFin));
    }
    
    @GetMapping("/producto/{productoId}/fecha")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorProductoYFecha(
            @PathVariable Long productoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientosPorProductoYFecha(productoId, fechaInicio, fechaFin));
    }
    
    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosPorUsuario(@PathVariable String usuario) {
        return ResponseEntity.ok(movimientoService.obtenerMovimientosPorUsuario(usuario));
    }
}