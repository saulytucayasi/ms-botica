package com.example.ms_producto.entity;

import com.example.ms_producto.dto.CategoriaDto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private Integer categoriaId;
    @Transient
    private CategoriaDto categoriaDto;

}
