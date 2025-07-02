package com.example.ms_ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer categoriaId;
}