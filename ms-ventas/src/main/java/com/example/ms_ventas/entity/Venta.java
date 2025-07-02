package com.example.ms_ventas.entity;

import com.example.ms_ventas.enums.EstadoVenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_venta", unique = true, nullable = false)
    private String numeroVenta;
    
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;
    
    @Column(name = "carrito_id")
    private Long carritoId;
    
    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoVenta estado;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "impuestos", precision = 10, scale = 2)
    private BigDecimal impuestos;
    
    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento;
    
    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VentaItem> items;
    
    @PrePersist
    public void prePersist() {
        fechaVenta = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoVenta.PENDIENTE;
        }
        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }
        if (impuestos == null) {
            impuestos = BigDecimal.ZERO;
        }
        if (descuento == null) {
            descuento = BigDecimal.ZERO;
        }
        if (total == null) {
            total = BigDecimal.ZERO;
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}