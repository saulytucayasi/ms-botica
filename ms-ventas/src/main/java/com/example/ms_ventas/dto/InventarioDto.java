package com.example.ms_ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioDto {
    private Long id;
    private Long productoId;
    private Integer cantidad;
    private Integer stockMinimo;
    private Integer stockMaximo;
}