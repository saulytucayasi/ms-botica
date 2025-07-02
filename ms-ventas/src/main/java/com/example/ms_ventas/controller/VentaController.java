package com.example.ms_ventas.controller;

import com.example.ms_ventas.dto.ActualizarEstadoRequest;
import com.example.ms_ventas.dto.CancelarVentaRequest;
import com.example.ms_ventas.dto.CrearVentaRequest;
import com.example.ms_ventas.entity.Venta;
import com.example.ms_ventas.entity.VentaItem;
import com.example.ms_ventas.enums.EstadoVenta;
import com.example.ms_ventas.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ventas", description = "Gestión de ventas y órdenes")
public class VentaController {
    
    private final VentaService ventaService;
    
    @PostMapping
    @Operation(summary = "Crear nueva venta desde carrito")
    public ResponseEntity<Venta> crearVenta(@Valid @RequestBody CrearVentaRequest request) {
        log.info("Creando venta para cliente: {} desde carrito: {}", request.getClienteId(), request.getCarritoId());
        Venta venta = ventaService.crearVentaDesdeCarrito(request.getClienteId(), request.getCarritoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(venta);
    }
    
    @GetMapping("/{ventaId}")
    @Operation(summary = "Obtener venta por ID")
    public ResponseEntity<Venta> obtenerVenta(
            @Parameter(description = "ID de la venta") @PathVariable Long ventaId) {
        log.info("Obteniendo venta: {}", ventaId);
        Venta venta = ventaService.obtenerVentaPorId(ventaId);
        return ResponseEntity.ok(venta);
    }
    
    @GetMapping("/numero/{numeroVenta}")
    @Operation(summary = "Obtener venta por número")
    public ResponseEntity<Venta> obtenerVentaPorNumero(
            @Parameter(description = "Número de venta") @PathVariable String numeroVenta) {
        log.info("Obteniendo venta por número: {}", numeroVenta);
        Optional<Venta> venta = ventaService.obtenerVentaPorNumero(numeroVenta);
        return venta.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener ventas por cliente")
    public ResponseEntity<List<Venta>> obtenerVentasCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        log.info("Obteniendo ventas del cliente: {}", clienteId);
        List<Venta> ventas = ventaService.obtenerVentasPorCliente(clienteId);
        return ResponseEntity.ok(ventas);
    }
    
    @GetMapping("/cliente/{clienteId}/paginado")
    @Operation(summary = "Obtener ventas por cliente paginado")
    public ResponseEntity<Page<Venta>> obtenerVentasClientePaginado(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId,
            Pageable pageable) {
        log.info("Obteniendo ventas paginadas del cliente: {}", clienteId);
        Page<Venta> ventas = ventaService.obtenerVentasPorClientePaginado(clienteId, pageable);
        return ResponseEntity.ok(ventas);
    }
    
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener ventas por estado")
    public ResponseEntity<List<Venta>> obtenerVentasPorEstado(
            @Parameter(description = "Estado de la venta") @PathVariable EstadoVenta estado) {
        log.info("Obteniendo ventas con estado: {}", estado);
        List<Venta> ventas = ventaService.obtenerVentasPorEstado(estado);
        return ResponseEntity.ok(ventas);
    }
    
    @GetMapping("/fecha")
    @Operation(summary = "Obtener ventas por rango de fechas")
    public ResponseEntity<List<Venta>> obtenerVentasPorFecha(
            @Parameter(description = "Fecha inicio") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha fin") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.info("Obteniendo ventas entre {} y {}", fechaInicio, fechaFin);
        List<Venta> ventas = ventaService.obtenerVentasPorFecha(fechaInicio, fechaFin);
        return ResponseEntity.ok(ventas);
    }
    
    @PutMapping("/{ventaId}/estado")
    @Operation(summary = "Actualizar estado de venta")
    public ResponseEntity<Venta> actualizarEstado(
            @Parameter(description = "ID de la venta") @PathVariable Long ventaId,
            @Valid @RequestBody ActualizarEstadoRequest request) {
        log.info("Actualizando estado de venta {} a {}", ventaId, request.getEstado());
        Venta venta = ventaService.actualizarEstadoVenta(ventaId, request.getEstado());
        return ResponseEntity.ok(venta);
    }
    
    @PutMapping("/{ventaId}/cancelar")
    @Operation(summary = "Cancelar venta")
    public ResponseEntity<Venta> cancelarVenta(
            @Parameter(description = "ID de la venta") @PathVariable Long ventaId,
            @Valid @RequestBody CancelarVentaRequest request) {
        log.info("Cancelando venta {} con motivo: {}", ventaId, request.getMotivo());
        Venta venta = ventaService.cancelarVenta(ventaId, request.getMotivo());
        return ResponseEntity.ok(venta);
    }
    
    @PostMapping("/{ventaId}/procesar")
    @Operation(summary = "Procesar venta")
    public ResponseEntity<Void> procesarVenta(
            @Parameter(description = "ID de la venta") @PathVariable Long ventaId) {
        log.info("Procesando venta: {}", ventaId);
        ventaService.procesarVenta(ventaId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{ventaId}/items")
    @Operation(summary = "Obtener items de venta")
    public ResponseEntity<List<VentaItem>> obtenerItemsVenta(
            @Parameter(description = "ID de la venta") @PathVariable Long ventaId) {
        log.info("Obteniendo items de venta: {}", ventaId);
        List<VentaItem> items = ventaService.obtenerItemsVenta(ventaId);
        return ResponseEntity.ok(items);
    }
}