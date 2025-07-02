package com.example.ms_carrito.controller;

import com.example.ms_carrito.dto.ActualizarCantidadRequest;
import com.example.ms_carrito.dto.AgregarProductoRequest;
import com.example.ms_carrito.entity.Carrito;
import com.example.ms_carrito.entity.CarritoItem;
import com.example.ms_carrito.service.CarritoService;
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

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Carrito", description = "Gestión del carrito de compras")
public class CarritoController {
    
    private final CarritoService carritoService;
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener carrito por cliente")
    public ResponseEntity<Carrito> obtenerCarritoPorCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        log.info("Obteniendo carrito para cliente: {}", clienteId);
        Carrito carrito = carritoService.obtenerCarritoPorCliente(clienteId);
        return ResponseEntity.ok(carrito);
    }
    
    @GetMapping("/session/{sessionId}")
    @Operation(summary = "Obtener carrito por sesión")
    public ResponseEntity<Carrito> obtenerCarritoPorSession(
            @Parameter(description = "ID de sesión") @PathVariable String sessionId) {
        log.info("Obteniendo carrito para sesión: {}", sessionId);
        Carrito carrito = carritoService.obtenerCarritoPorSession(sessionId);
        return ResponseEntity.ok(carrito);
    }
    
    @PostMapping("/cliente/{clienteId}/agregar")
    @Operation(summary = "Agregar producto al carrito")
    public ResponseEntity<Carrito> agregarProducto(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId,
            @Valid @RequestBody AgregarProductoRequest request) {
        log.info("Agregando producto {} al carrito del cliente {}", request.getProductoId(), clienteId);
        Carrito carrito = carritoService.agregarProducto(clienteId, request.getProductoId(), request.getCantidad());
        return ResponseEntity.ok(carrito);
    }
    
    @PostMapping("/session/{sessionId}/agregar")
    @Operation(summary = "Agregar producto al carrito por sesión")
    public ResponseEntity<Carrito> agregarProductoPorSession(
            @Parameter(description = "ID de sesión") @PathVariable String sessionId,
            @Valid @RequestBody AgregarProductoRequest request) {
        log.info("Agregando producto {} al carrito de sesión {}", request.getProductoId(), sessionId);
        Carrito carrito = carritoService.agregarProductoPorSession(sessionId, request.getProductoId(), request.getCantidad());
        return ResponseEntity.ok(carrito);
    }
    
    @PutMapping("/cliente/{clienteId}/producto/{productoId}")
    @Operation(summary = "Actualizar cantidad de producto")
    public ResponseEntity<Carrito> actualizarCantidad(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId,
            @Parameter(description = "ID del producto") @PathVariable Long productoId,
            @Valid @RequestBody ActualizarCantidadRequest request) {
        log.info("Actualizando cantidad del producto {} para cliente {}", productoId, clienteId);
        Carrito carrito = carritoService.actualizarCantidad(clienteId, productoId, request.getCantidad());
        return ResponseEntity.ok(carrito);
    }
    
    @DeleteMapping("/cliente/{clienteId}/producto/{productoId}")
    @Operation(summary = "Eliminar producto del carrito")
    public ResponseEntity<Carrito> eliminarProducto(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId,
            @Parameter(description = "ID del producto") @PathVariable Long productoId) {
        log.info("Eliminando producto {} del carrito del cliente {}", productoId, clienteId);
        Carrito carrito = carritoService.eliminarProducto(clienteId, productoId);
        return ResponseEntity.ok(carrito);
    }
    
    @DeleteMapping("/cliente/{clienteId}/limpiar")
    @Operation(summary = "Limpiar carrito completamente")
    public ResponseEntity<Void> limpiarCarrito(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        log.info("Limpiando carrito del cliente {}", clienteId);
        carritoService.limpiarCarrito(clienteId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{carritoId}/items")
    @Operation(summary = "Obtener items del carrito")
    public ResponseEntity<List<CarritoItem>> obtenerItemsCarrito(
            @Parameter(description = "ID del carrito") @PathVariable Long carritoId) {
        log.info("Obteniendo items del carrito: {}", carritoId);
        List<CarritoItem> items = carritoService.obtenerItemsCarrito(carritoId);
        return ResponseEntity.ok(items);
    }
    
    @PutMapping("/{carritoId}/desactivar")
    @Operation(summary = "Desactivar carrito")
    public ResponseEntity<Void> desactivarCarrito(
            @Parameter(description = "ID del carrito") @PathVariable Long carritoId) {
        log.info("Desactivando carrito: {}", carritoId);
        carritoService.desactivarCarrito(carritoId);
        return ResponseEntity.noContent().build();
    }
}