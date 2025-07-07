package com.example.ms_compras.service.impl;

import com.example.ms_compras.entity.RecepcionItem;
import com.example.ms_compras.entity.RecepcionMercancia;
import com.example.ms_compras.enums.EstadoRecepcion;
import com.example.ms_compras.feign.InventarioFeign;
import com.example.ms_compras.repository.RecepcionMercanciaRepository;
import com.example.ms_compras.service.RecepcionMercanciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecepcionMercanciaServiceImpl implements RecepcionMercanciaService {
    
    private final RecepcionMercanciaRepository recepcionMercanciaRepository;
    private final InventarioFeign inventarioFeign;
    
    @Override
    @Transactional(readOnly = true)
    public List<RecepcionMercancia> listar() {
        return recepcionMercanciaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RecepcionMercancia> buscarPorId(Long id) {
        return recepcionMercanciaRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RecepcionMercancia> buscarPorNumeroRecepcion(String numeroRecepcion) {
        return recepcionMercanciaRepository.findByNumeroRecepcion(numeroRecepcion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecepcionMercancia> buscarPorOrdenCompra(Long ordenCompraId) {
        return recepcionMercanciaRepository.findByOrdenCompraId(ordenCompraId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecepcionMercancia> buscarPorEstado(EstadoRecepcion estado) {
        return recepcionMercanciaRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecepcionMercancia> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return recepcionMercanciaRepository.findByFechaRecepcionBetween(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional
    public RecepcionMercancia crear(RecepcionMercancia recepcionMercancia) {
        recepcionMercancia.setNumeroRecepcion(generarNumeroRecepcion());
        recepcionMercancia.setEstado(EstadoRecepcion.PENDIENTE);
        
        if (recepcionMercancia.getItems() != null && !recepcionMercancia.getItems().isEmpty()) {
            for (RecepcionItem item : recepcionMercancia.getItems()) {
                item.setRecepcionMercancia(recepcionMercancia);
            }
        }
        
        return recepcionMercanciaRepository.save(recepcionMercancia);
    }
    
    @Override
    @Transactional
    public RecepcionMercancia actualizar(Long id, RecepcionMercancia recepcionMercancia) {
        Optional<RecepcionMercancia> recepcionExistente = recepcionMercanciaRepository.findById(id);
        if (recepcionExistente.isPresent()) {
            RecepcionMercancia recepcionActualizada = recepcionExistente.get();
            recepcionActualizada.setOrdenCompraId(recepcionMercancia.getOrdenCompraId());
            recepcionActualizada.setObservaciones(recepcionMercancia.getObservaciones());
            
            if (recepcionMercancia.getItems() != null && !recepcionMercancia.getItems().isEmpty()) {
                recepcionActualizada.getItems().clear();
                for (RecepcionItem item : recepcionMercancia.getItems()) {
                    item.setRecepcionMercancia(recepcionActualizada);
                    recepcionActualizada.getItems().add(item);
                }
            }
            
            return recepcionMercanciaRepository.save(recepcionActualizada);
        }
        throw new RuntimeException("Recepción de mercancía no encontrada con ID: " + id);
    }
    
    @Override
    @Transactional
    public void cambiarEstado(Long id, EstadoRecepcion estado) {
        Optional<RecepcionMercancia> recepcion = recepcionMercanciaRepository.findById(id);
        if (recepcion.isPresent()) {
            recepcion.get().setEstado(estado);
            recepcionMercanciaRepository.save(recepcion.get());
        } else {
            throw new RuntimeException("Recepción de mercancía no encontrada con ID: " + id);
        }
    }
    
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (recepcionMercanciaRepository.existsById(id)) {
            recepcionMercanciaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Recepción de mercancía no encontrada con ID: " + id);
        }
    }
    
    @Override
    public String generarNumeroRecepcion() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "RC-" + timestamp;
    }
    
    @Override
    @Transactional
    public void procesarRecepcion(Long recepcionId) {
        Optional<RecepcionMercancia> recepcionOpt = recepcionMercanciaRepository.findById(recepcionId);
        if (recepcionOpt.isPresent()) {
            RecepcionMercancia recepcion = recepcionOpt.get();
            
            for (RecepcionItem item : recepcion.getItems()) {
                if (item.getCantidadRecibida() > 0) {
                    try {
                        inventarioFeign.actualizarStock(
                            item.getProductoId(),
                            item.getCantidadRecibida(),
                            "COMPRA"
                        );
                        log.info("Stock actualizado para producto {} con cantidad {}", 
                                item.getProductoId(), item.getCantidadRecibida());
                    } catch (Exception e) {
                        log.error("Error al actualizar stock para producto {}: {}", 
                                item.getProductoId(), e.getMessage());
                        throw new RuntimeException("Error al actualizar inventario: " + e.getMessage());
                    }
                }
            }
            
            recepcion.setEstado(EstadoRecepcion.COMPLETA);
            recepcionMercanciaRepository.save(recepcion);
        } else {
            throw new RuntimeException("Recepción de mercancía no encontrada con ID: " + recepcionId);
        }
    }
}