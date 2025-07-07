package com.example.ms_compras.entity;

import com.example.ms_compras.enums.EstadoRecepcion;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recepciones_mercancia")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecepcionMercancia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_recepcion", unique = true, nullable = false)
    private String numeroRecepcion;
    
    @Column(name = "orden_compra_id", nullable = false)
    private Long ordenCompraId;
    
    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoRecepcion estado;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @OneToMany(mappedBy = "recepcionMercancia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<RecepcionItem> items;
    
    @PrePersist
    public void prePersist() {
        fechaRecepcion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoRecepcion.PENDIENTE;
        }
    }
}