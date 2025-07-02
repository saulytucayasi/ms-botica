package com.example.ms_ventas.dto;

import com.example.ms_ventas.enums.EstadoVenta;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarEstadoRequest {
    
    @NotNull(message = "El estado es requerido")
    private EstadoVenta estado;
    
    private String observaciones;
}