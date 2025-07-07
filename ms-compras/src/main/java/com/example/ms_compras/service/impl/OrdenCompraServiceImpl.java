package com.example.ms_compras.service.impl;

import com.example.ms_compras.entity.OrdenCompra;
import com.example.ms_compras.entity.OrdenCompraItem;
import com.example.ms_compras.enums.EstadoOrden;
import com.example.ms_compras.repository.OrdenCompraRepository;
import com.example.ms_compras.service.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdenCompraServiceImpl implements OrdenCompraService {
    
    private final OrdenCompraRepository ordenCompraRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> listar() {
        return ordenCompraRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompra> buscarPorId(Long id) {
        return ordenCompraRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompra> buscarPorNumeroOrden(String numeroOrden) {
        return ordenCompraRepository.findByNumeroOrden(numeroOrden);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> buscarPorProveedor(Long proveedorId) {
        return ordenCompraRepository.findByProveedorId(proveedorId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> buscarPorEstado(EstadoOrden estado) {
        return ordenCompraRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ordenCompraRepository.findByFechaOrdenBetween(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompra> buscarOrdenesVencidas() {
        return ordenCompraRepository.findOrdenesVencidas(LocalDateTime.now());
    }
    
    @Override
    @Transactional
    public OrdenCompra crear(OrdenCompra ordenCompra) {
        ordenCompra.setNumeroOrden(generarNumeroOrden());
        ordenCompra.setEstado(EstadoOrden.PENDIENTE);
        
        if (ordenCompra.getItems() != null && !ordenCompra.getItems().isEmpty()) {
            for (OrdenCompraItem item : ordenCompra.getItems()) {
                item.setOrdenCompra(ordenCompra);
            }
            calcularTotales(ordenCompra);
        }
        
        return ordenCompraRepository.save(ordenCompra);
    }
    
    @Override
    @Transactional
    public OrdenCompra actualizar(Long id, OrdenCompra ordenCompra) {
        Optional<OrdenCompra> ordenExistente = ordenCompraRepository.findById(id);
        if (ordenExistente.isPresent()) {
            OrdenCompra ordenActualizada = ordenExistente.get();
            ordenActualizada.setProveedorId(ordenCompra.getProveedorId());
            ordenActualizada.setFechaEntregaEsperada(ordenCompra.getFechaEntregaEsperada());
            ordenActualizada.setObservaciones(ordenCompra.getObservaciones());
            
            if (ordenCompra.getItems() != null && !ordenCompra.getItems().isEmpty()) {
                ordenActualizada.getItems().clear();
                for (OrdenCompraItem item : ordenCompra.getItems()) {
                    item.setOrdenCompra(ordenActualizada);
                    ordenActualizada.getItems().add(item);
                }
                calcularTotales(ordenActualizada);
            }
            
            return ordenCompraRepository.save(ordenActualizada);
        }
        throw new RuntimeException("Orden de compra no encontrada con ID: " + id);
    }
    
    @Override
    @Transactional
    public void cambiarEstado(Long id, EstadoOrden estado) {
        Optional<OrdenCompra> orden = ordenCompraRepository.findById(id);
        if (orden.isPresent()) {
            orden.get().setEstado(estado);
            ordenCompraRepository.save(orden.get());
        } else {
            throw new RuntimeException("Orden de compra no encontrada con ID: " + id);
        }
    }
    
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (ordenCompraRepository.existsById(id)) {
            ordenCompraRepository.deleteById(id);
        } else {
            throw new RuntimeException("Orden de compra no encontrada con ID: " + id);
        }
    }
    
    @Override
    public String generarNumeroOrden() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "OC-" + timestamp;
    }
    
    private void calcularTotales(OrdenCompra ordenCompra) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (OrdenCompraItem item : ordenCompra.getItems()) {
            if (item.getSubtotal() != null) {
                subtotal = subtotal.add(item.getSubtotal());
            }
        }
        
        ordenCompra.setSubtotal(subtotal);
        
        BigDecimal impuestos = subtotal.multiply(new BigDecimal("0.18"));
        ordenCompra.setImpuestos(impuestos);
        
        BigDecimal total = subtotal.add(impuestos);
        ordenCompra.setTotal(total);
    }
}