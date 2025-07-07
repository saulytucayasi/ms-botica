package com.example.ms_compras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventarioDto {
    
    private Long id;
    private Long productoId;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private BigDecimal costoPromedio;
    private String ubicacion;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}