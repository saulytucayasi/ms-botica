package com.example.ms_producto.service;

import com.example.ms_producto.entity.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> findAll();
    Producto findById(int id);
    Producto Save(Producto producto);
    Producto update(Producto producto);
}
