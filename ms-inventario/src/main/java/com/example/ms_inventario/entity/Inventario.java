package com.example.ms_inventario.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "inventario")
public class Inventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;
    
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 5;
    
    @Column(name = "stock_maximo", nullable = false)
    private Integer stockMaximo = 100;
    
    @Column(name = "costo_promedio", precision = 10, scale = 2)
    private BigDecimal costoPromedio = BigDecimal.ZERO;
    
    @Column(name = "ubicacion")
    private String ubicacion;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "estado")
    private String estado = "ACTIVO";
}