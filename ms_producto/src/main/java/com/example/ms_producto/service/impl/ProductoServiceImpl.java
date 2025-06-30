package com.example.ms_producto.service.impl;

import com.example.ms_producto.entity.Producto;
import com.example.ms_producto.feign.CatalogoFeign;
import com.example.ms_producto.repository.ProductoRepository;
import com.example.ms_producto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private CatalogoFeign catalogoFeign;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Producto findById(int id) {
        Producto producto = productoRepository.findById(id).get();
        producto.setCategoriaDto(catalogoFeign.buscarCategoria(producto.getCategoriaId()).getBody());
        return productoRepository.findById(id).get();
    }

    @Override
    public Producto Save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto update(Producto producto) {
        return productoRepository.save(producto);
    }
}
