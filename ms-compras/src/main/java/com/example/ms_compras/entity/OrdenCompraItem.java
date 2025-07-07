package com.example.ms_compras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "orden_compra_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenCompraItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    @JsonBackReference
    private OrdenCompra ordenCompra;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
}