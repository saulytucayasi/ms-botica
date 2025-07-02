package com.example.ms_carrito.service.impl;

import com.example.ms_carrito.dto.ProductoDto;
import com.example.ms_carrito.entity.Carrito;
import com.example.ms_carrito.entity.CarritoItem;
import com.example.ms_carrito.feign.ProductoFeign;
import com.example.ms_carrito.repository.CarritoRepository;
import com.example.ms_carrito.repository.CarritoItemRepository;
import com.example.ms_carrito.service.CarritoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CarritoServiceImpl implements CarritoService {
    
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoFeign productoFeign;
    
    @Override
    public Carrito obtenerCarritoPorCliente(Long clienteId) {
        return carritoRepository.findByClienteIdWithItems(clienteId)
                .orElseGet(() -> crearNuevoCarrito(clienteId, null));
    }
    
    @Override
    public Carrito obtenerCarritoPorSession(String sessionId) {
        return carritoRepository.findBySessionIdAndActivoTrue(sessionId)
                .orElseGet(() -> crearNuevoCarrito(null, sessionId));
    }
    
    @Override
    public Carrito agregarProducto(Long clienteId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerCarritoPorCliente(clienteId);
        return agregarItemAlCarrito(carrito, productoId, cantidad);
    }
    
    @Override
    public Carrito agregarProductoPorSession(String sessionId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerCarritoPorSession(sessionId);
        return agregarItemAlCarrito(carrito, productoId, cantidad);
    }
    
    @Override
    public Carrito actualizarCantidad(Long clienteId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerCarritoPorCliente(clienteId);
        
        Optional<CarritoItem> itemExistente = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId);
        
        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            item.setCantidad(cantidad);
            item.calcularSubtotal();
            carritoItemRepository.save(item);
            
            calcularTotalCarrito(carrito);
            return carritoRepository.save(carrito);
        }
        
        throw new RuntimeException("Producto no encontrado en el carrito");
    }
    
    @Override
    public Carrito eliminarProducto(Long clienteId, Long productoId) {
        Carrito carrito = obtenerCarritoPorCliente(clienteId);
        carritoItemRepository.deleteByCarritoIdAndProductoId(carrito.getId(), productoId);
        
        calcularTotalCarrito(carrito);
        return carritoRepository.save(carrito);
    }
    
    @Override
    public void limpiarCarrito(Long clienteId) {
        Carrito carrito = obtenerCarritoPorCliente(clienteId);
        carritoItemRepository.deleteAllByCarritoId(carrito.getId());
        carrito.setTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);
    }
    
    @Override
    public void desactivarCarrito(Long carritoId) {
        carritoRepository.findById(carritoId).ifPresent(carrito -> {
            carrito.setActivo(false);
            carritoRepository.save(carrito);
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CarritoItem> obtenerItemsCarrito(Long carritoId) {
        return carritoItemRepository.findByCarritoId(carritoId);
    }
    
    private Carrito crearNuevoCarrito(Long clienteId, String sessionId) {
        Carrito carrito = Carrito.builder()
                .clienteId(clienteId)
                .sessionId(sessionId)
                .total(BigDecimal.ZERO)
                .activo(true)
                .items(new ArrayList<>())
                .build();
        
        return carritoRepository.save(carrito);
    }
    
    private Carrito agregarItemAlCarrito(Carrito carrito, Long productoId, Integer cantidad) {
        // Obtener precio del producto via Feign
        BigDecimal precioUnitario = obtenerPrecioProducto(productoId);
        
        Optional<CarritoItem> itemExistente = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId);
        
        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            item.calcularSubtotal();
            carritoItemRepository.save(item);
        } else {
            CarritoItem nuevoItem = CarritoItem.builder()
                    .carrito(carrito)
                    .productoId(productoId)
                    .cantidad(cantidad)
                    .precioUnitario(precioUnitario)
                    .build();
            
            nuevoItem.calcularSubtotal();
            CarritoItem itemGuardado = carritoItemRepository.save(nuevoItem);
            
            // Agregar el item a la lista del carrito si está inicializada
            if (carrito.getItems() != null) {
                carrito.getItems().add(itemGuardado);
            }
        }
        
        calcularTotalCarrito(carrito);
        return carritoRepository.save(carrito);
    }
    
    private void calcularTotalCarrito(Carrito carrito) {
        BigDecimal total = carritoItemRepository.findByCarritoId(carrito.getId())
                .stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        carrito.setTotal(total);
    }
    
    private BigDecimal obtenerPrecioProducto(Long productoId) {
        try {
            log.info("Obteniendo precio del producto: {}", productoId);
            ResponseEntity<ProductoDto> response = productoFeign.obtenerProducto(productoId.intValue());
            
            if (response.getBody() != null && response.getBody().getPrecio() != null) {
                return response.getBody().getPrecio();
            }
            
            log.warn("No se pudo obtener el precio del producto: {}", productoId);
            return BigDecimal.valueOf(10.00); // Precio por defecto
            
        } catch (Exception e) {
            log.error("Error al obtener precio del producto: {}", productoId, e);
            return BigDecimal.valueOf(10.00); // Precio por defecto en caso de error
        }
    }
}