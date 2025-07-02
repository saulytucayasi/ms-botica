package com.example.ms_carrito.service;

import com.example.ms_carrito.entity.Carrito;
import com.example.ms_carrito.entity.CarritoItem;

import java.util.List;
import java.util.Optional;

public interface CarritoService {
    
    Carrito obtenerCarritoPorCliente(Long clienteId);
    
    Carrito obtenerCarritoPorSession(String sessionId);
    
    Carrito agregarProducto(Long clienteId, Long productoId, Integer cantidad);
    
    Carrito agregarProductoPorSession(String sessionId, Long productoId, Integer cantidad);
    
    Carrito actualizarCantidad(Long clienteId, Long productoId, Integer cantidad);
    
    Carrito eliminarProducto(Long clienteId, Long productoId);
    
    void limpiarCarrito(Long clienteId);
    
    void desactivarCarrito(Long carritoId);
    
    List<CarritoItem> obtenerItemsCarrito(Long carritoId);
}