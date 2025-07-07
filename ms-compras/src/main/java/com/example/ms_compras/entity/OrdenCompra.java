package com.example.ms_compras.entity;

import com.example.ms_compras.enums.EstadoOrden;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes_compra")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenCompra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_orden", unique = true, nullable = false)
    private String numeroOrden;
    
    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;
    
    @Column(name = "fecha_orden")
    private LocalDateTime fechaOrden;
    
    @Column(name = "fecha_entrega_esperada")
    private LocalDateTime fechaEntregaEsperada;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOrden estado;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "impuestos", precision = 10, scale = 2)
    private BigDecimal impuestos;
    
    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrdenCompraItem> items;
    
    @PrePersist
    public void prePersist() {
        fechaOrden = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoOrden.PENDIENTE;
        }
        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }
        if (impuestos == null) {
            impuestos = BigDecimal.ZERO;
        }
        if (total == null) {
            total = BigDecimal.ZERO;
        }
    }
}