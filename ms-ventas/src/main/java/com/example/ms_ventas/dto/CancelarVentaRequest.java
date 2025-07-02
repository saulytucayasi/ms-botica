package com.example.ms_ventas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelarVentaRequest {
    
    @NotBlank(message = "El motivo de cancelación es requerido")
    private String motivo;
}