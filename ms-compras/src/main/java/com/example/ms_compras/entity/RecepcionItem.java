package com.example.ms_compras.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recepcion_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecepcionItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcion_mercancia_id", nullable = false)
    @JsonBackReference
    private RecepcionMercancia recepcionMercancia;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(name = "cantidad_esperada", nullable = false)
    private Integer cantidadEsperada;
    
    @Column(name = "cantidad_recibida", nullable = false)
    private Integer cantidadRecibida;
    
    @Column(name = "cantidad_danada")
    private Integer cantidadDanada = 0;
    
    @Column(name = "observaciones", length = 200)
    private String observaciones;
}