package com.example.ms_ventas.controller;

import com.example.ms_ventas.dto.CrearVentaRequest;
import com.example.ms_ventas.entity.Venta;
import com.example.ms_ventas.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ventas", description = "Gestión de ventas")
public class VentaController {
    
    private final VentaService ventaService;
    
    @PostMapping("/realizar")
    @Operation(summary = "Realizar venta desde carrito (procesa automáticamente)")
    public ResponseEntity<Venta> realizarVenta(@Valid @RequestBody CrearVentaRequest request) {
        try {
            // Validar que se proporcione cliente o sesión
            if (request.getClienteId() == null && request.getSessionId() == null) {
                throw new RuntimeException("Debe proporcionar clienteId o sessionId");
            }
            
            String identifier = request.getClienteId() != null ? 
                               "cliente: " + request.getClienteId() : 
                               "sesión: " + request.getSessionId();
            
            log.info("Realizando venta para {} desde carrito: {}", identifier, request.getCarritoId());
            
            // Crear venta y procesarla automáticamente
            Venta venta;
            if (request.getClienteId() != null) {
                venta = ventaService.crearVentaDesdeCarrito(request.getClienteId(), request.getCarritoId());
            } else {
                venta = ventaService.crearVentaDesdeCarritoPorSession(request.getSessionId(), request.getCarritoId());
            }
            
            ventaService.procesarVenta(venta.getId()); // Procesa automáticamente y reduce stock
            
            // Obtener venta actualizada con estado CONFIRMADA
            venta = ventaService.obtenerVentaPorId(venta.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(venta);
            
        } catch (Exception e) {
            log.error("Error al realizar venta: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar la venta: " + e.getMessage());
        }
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
    
    @GetMapping
    @Operation(summary = "Listar todas las ventas realizadas")
    public ResponseEntity<List<Venta>> listarVentas() {
        log.info("Listando todas las ventas");
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener ventas por cliente")
    public ResponseEntity<List<Venta>> obtenerVentasCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        log.info("Obteniendo ventas del cliente: {}", clienteId);
        List<Venta> ventas = ventaService.obtenerVentasPorCliente(clienteId);
        return ResponseEntity.ok(ventas);
    }
}