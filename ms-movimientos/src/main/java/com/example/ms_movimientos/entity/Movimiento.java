package com.example.ms_movimientos.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "movimientos")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(name = "tipo_movimiento", nullable = false)
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(name = "costo_total", precision = 10, scale = 2)
    private BigDecimal costoTotal;
    
    @Column(name = "motivo")
    private String motivo;
    
    @Column(name = "documento_referencia")
    private String documentoReferencia;
    
    @Column(name = "usuario")
    private String usuario;
    
    @CreationTimestamp
    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "stock_anterior")
    private Integer stockAnterior;
    
    @Column(name = "stock_actual")
    private Integer stockActual;
}