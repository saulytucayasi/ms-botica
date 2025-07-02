package com.example.ms_ventas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearVentaRequest {
    
    @NotNull(message = "El ID del cliente es requerido")
    private Long clienteId;
    
    @NotNull(message = "El ID del carrito es requerido")
    private Long carritoId;
    
    private String observaciones;
}