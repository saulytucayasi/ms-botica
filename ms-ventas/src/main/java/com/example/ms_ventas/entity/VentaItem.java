package com.example.ms_ventas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "venta_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonBackReference
    private Venta venta;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(name = "producto_nombre", nullable = false)
    private String productoNombre;
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(name = "descuento_item", precision = 10, scale = 2)
    private BigDecimal descuentoItem;
    
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;
    
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (precioUnitario != null && cantidad != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
            if (descuentoItem != null) {
                subtotal = subtotal.subtract(descuentoItem);
            }
        }
        if (descuentoItem == null) {
            descuentoItem = BigDecimal.ZERO;
        }
    }
}