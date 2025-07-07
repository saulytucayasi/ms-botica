package com.example.ms_compras.service.impl;

import com.example.ms_compras.entity.Proveedor;
import com.example.ms_compras.repository.ProveedorRepository;
import com.example.ms_compras.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {
    
    private final ProveedorRepository proveedorRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorId(Long id) {
        return proveedorRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.buscarPorNombre(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarActivos() {
        return proveedorRepository.findProveedoresActivos();
    }
    
    @Override
    @Transactional
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }
    
    @Override
    @Transactional
    public Proveedor actualizar(Long id, Proveedor proveedor) {
        Optional<Proveedor> proveedorExistente = proveedorRepository.findById(id);
        if (proveedorExistente.isPresent()) {
            Proveedor proveedorActualizado = proveedorExistente.get();
            proveedorActualizado.setNombre(proveedor.getNombre());
            proveedorActualizado.setContacto(proveedor.getContacto());
            proveedorActualizado.setEmail(proveedor.getEmail());
            proveedorActualizado.setTelefono(proveedor.getTelefono());
            proveedorActualizado.setDireccion(proveedor.getDireccion());
            proveedorActualizado.setRuc(proveedor.getRuc());
            proveedorActualizado.setEstado(proveedor.getEstado());
            return proveedorRepository.save(proveedorActualizado);
        }
        throw new RuntimeException("Proveedor no encontrado con ID: " + id);
    }
    
    @Override
    @Transactional
    public void eliminar(Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }
    
    @Override
    @Transactional
    public void cambiarEstado(Long id, String estado) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);
        if (proveedor.isPresent()) {
            proveedor.get().setEstado(estado);
            proveedorRepository.save(proveedor.get());
        } else {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }
}