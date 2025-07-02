package com.example.ms_ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDto {
    private Long id;
    private Long clienteId;
    private String sessionId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private BigDecimal total;
    private Boolean activo;
    private List<CarritoItemDto> items;
}