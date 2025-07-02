package com.example.ms_ventas.service.impl;

import com.example.ms_ventas.dto.CarritoDto;
import com.example.ms_ventas.dto.CarritoItemDto;
import com.example.ms_ventas.dto.InventarioDto;
import com.example.ms_ventas.dto.ProductoDto;
import com.example.ms_ventas.entity.Venta;
import com.example.ms_ventas.entity.VentaItem;
import com.example.ms_ventas.enums.EstadoVenta;
import com.example.ms_ventas.feign.CarritoFeign;
import com.example.ms_ventas.feign.InventarioFeign;
import com.example.ms_ventas.feign.ProductoFeign;
import com.example.ms_ventas.repository.VentaRepository;
import com.example.ms_ventas.repository.VentaItemRepository;
import com.example.ms_ventas.service.VentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VentaServiceImpl implements VentaService {
    
    private final VentaRepository ventaRepository;
    private final VentaItemRepository ventaItemRepository;
    private final CarritoFeign carritoFeign;
    private final ProductoFeign productoFeign;
    private final InventarioFeign inventarioFeign;
    
    @Override
    public Venta crearVentaDesdeCarrito(Long clienteId, Long carritoId) {
        log.info("Creando venta desde carrito {} para cliente {}", carritoId, clienteId);
        
        // Obtener datos del carrito por cliente
        ResponseEntity<CarritoDto> carritoResponse = carritoFeign.obtenerCarritoPorCliente(clienteId);
        CarritoDto carrito = carritoResponse.getBody();
        
        if (carrito == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("Carrito vacío o no encontrado");
        }
        
        String numeroVenta = generarNumeroVenta();
        
        Venta venta = Venta.builder()
                .numeroVenta(numeroVenta)
                .clienteId(clienteId)
                .carritoId(carritoId)
                .estado(EstadoVenta.PENDIENTE)
                .subtotal(BigDecimal.ZERO)
                .impuestos(BigDecimal.ZERO)
                .descuento(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();
        
        venta = ventaRepository.save(venta);
        
        // Crear items desde carrito
        crearItemsDesdeCarrito(venta, carrito);
        
        // Calcular totales
        calcularTotalesVenta(venta);
        venta = ventaRepository.save(venta);
        
        // Desactivar carrito
        carritoFeign.desactivarCarrito(carritoId);
        
        log.info("Venta creada con ID: {} y número: {}", venta.getId(), venta.getNumeroVenta());
        return venta;
    }
    
    @Override
    public Venta crearVentaDesdeCarritoPorSession(String sessionId, Long carritoId) {
        log.info("Creando venta desde carrito {} para sesión {}", carritoId, sessionId);
        
        // Obtener datos del carrito por sesión
        ResponseEntity<CarritoDto> carritoResponse = carritoFeign.obtenerCarritoPorSession(sessionId);
        CarritoDto carrito = carritoResponse.getBody();
        
        if (carrito == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("Carrito vacío o no encontrado");
        }
        
        String numeroVenta = generarNumeroVenta();
        
        Venta venta = Venta.builder()
                .numeroVenta(numeroVenta)
                .clienteId(null) // Cliente anónimo
                .carritoId(carritoId)
                .estado(EstadoVenta.PENDIENTE)
                .subtotal(BigDecimal.ZERO)
                .impuestos(BigDecimal.ZERO)
                .descuento(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();
        
        venta = ventaRepository.save(venta);
        
        // Crear items desde carrito
        crearItemsDesdeCarrito(venta, carrito);
        
        // Calcular totales
        calcularTotalesVenta(venta);
        venta = ventaRepository.save(venta);
        
        // Desactivar carrito
        carritoFeign.desactivarCarrito(carritoId);
        
        log.info("Venta anónima creada con ID: {} y número: {}", venta.getId(), venta.getNumeroVenta());
        return venta;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Venta obtenerVentaPorId(Long ventaId) {
        return ventaRepository.findByIdWithItems(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> obtenerVentaPorNumero(String numeroVenta) {
        return ventaRepository.findByNumeroVenta(numeroVenta);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Venta> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteIdOrderByFechaVentaDesc(clienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAllByOrderByFechaVentaDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Venta> obtenerVentasPorClientePaginado(Long clienteId, Pageable pageable) {
        return ventaRepository.findByClienteId(clienteId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Venta> obtenerVentasPorEstado(EstadoVenta estado) {
        return ventaRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Venta> obtenerVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }
    
    @Override
    public Venta actualizarEstadoVenta(Long ventaId, EstadoVenta nuevoEstado) {
        log.info("Actualizando estado de venta {} a {}", ventaId, nuevoEstado);
        
        Venta venta = obtenerVentaPorId(ventaId);
        
        if (venta.getEstado() == EstadoVenta.CANCELADA) {
            throw new RuntimeException("No se puede cambiar el estado de una venta cancelada");
        }
        
        venta.setEstado(nuevoEstado);
        return ventaRepository.save(venta);
    }
    
    @Override
    public Venta cancelarVenta(Long ventaId, String motivo) {
        log.info("Cancelando venta {} con motivo: {}", ventaId, motivo);
        
        Venta venta = obtenerVentaPorId(ventaId);
        
        if (venta.getEstado() == EstadoVenta.ENTREGADA) {
            throw new RuntimeException("No se puede cancelar una venta ya entregada");
        }
        
        venta.setEstado(EstadoVenta.CANCELADA);
        venta.setObservaciones(motivo);
        
        // Restaurar inventario
        restaurarInventarioVenta(venta);
        
        return ventaRepository.save(venta);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<VentaItem> obtenerItemsVenta(Long ventaId) {
        return ventaItemRepository.findByVentaId(ventaId);
    }
    
    @Override
    public void procesarVenta(Long ventaId) {
        log.info("Procesando venta: {}", ventaId);
        
        Venta venta = obtenerVentaPorId(ventaId);
        
        if (venta.getEstado() != EstadoVenta.PENDIENTE) {
            throw new RuntimeException("Solo se pueden procesar ventas pendientes");
        }
        
        // Validar stock disponible y reducir inventario
        validarYReducirInventario(venta);
        
        venta.setEstado(EstadoVenta.CONFIRMADA);
        ventaRepository.save(venta);
        
        log.info("Venta {} procesada exitosamente", ventaId);
    }
    
    private String generarNumeroVenta() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "VEN-" + timestamp;
    }
    
    private void calcularTotalesVenta(Venta venta) {
        List<VentaItem> items = ventaItemRepository.findByVentaId(venta.getId());
        
        BigDecimal subtotal = items.stream()
                .map(VentaItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal impuestos = subtotal.multiply(BigDecimal.valueOf(0.16)); // 16% IVA
        BigDecimal total = subtotal.add(impuestos).subtract(venta.getDescuento());
        
        venta.setSubtotal(subtotal);
        venta.setImpuestos(impuestos);
        venta.setTotal(total);
    }
    
    private void crearItemsDesdeCarrito(Venta venta, CarritoDto carrito) {
        for (CarritoItemDto carritoItem : carrito.getItems()) {
            // Obtener información del producto
            ResponseEntity<ProductoDto> productoResponse = productoFeign.obtenerProducto(carritoItem.getProductoId().intValue());
            ProductoDto producto = productoResponse.getBody();
            
            VentaItem ventaItem = VentaItem.builder()
                    .venta(venta)
                    .productoId(carritoItem.getProductoId())
                    .productoNombre(producto != null ? producto.getNombre() : "Producto no disponible")
                    .cantidad(carritoItem.getCantidad())
                    .precioUnitario(carritoItem.getPrecioUnitario())
                    .descuentoItem(BigDecimal.ZERO)
                    .build();
            
            ventaItem.calcularSubtotal();
            ventaItemRepository.save(ventaItem);
        }
    }
    
    private void validarYReducirInventario(Venta venta) {
        List<VentaItem> items = ventaItemRepository.findByVentaId(venta.getId());
        
        for (VentaItem item : items) {
            // Validar stock disponible
            ResponseEntity<InventarioDto> inventarioResponse = inventarioFeign.obtenerInventarioPorProducto(item.getProductoId());
            InventarioDto inventario = inventarioResponse.getBody();
            
            if (inventario != null && inventario.getStockActual() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para producto: " + item.getProductoNombre());
            }
            
            // Reducir inventario
            Map<String, Object> request = new HashMap<>();
            request.put("productoId", item.getProductoId());
            request.put("cantidad", item.getCantidad());
            request.put("tipoMovimiento", "SALIDA");
            
            inventarioFeign.actualizarStock(request);
        }
    }
    
    private void restaurarInventarioVenta(Venta venta) {
        List<VentaItem> items = ventaItemRepository.findByVentaId(venta.getId());
        
        for (VentaItem item : items) {
            try {
                Map<String, Object> request = new HashMap<>();
                request.put("productoId", item.getProductoId());
                request.put("cantidad", item.getCantidad());
                request.put("tipoMovimiento", "ENTRADA");
                
                inventarioFeign.actualizarStock(request);
            } catch (Exception e) {
                log.error("Error al restaurar inventario para producto: {}", item.getProductoId(), e);
            }
        }
    }
}