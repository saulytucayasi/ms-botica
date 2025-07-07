package com.example.ms_compras.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "contacto", length = 100)
    private String contacto;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "direccion", length = 200)
    private String direccion;
    
    @Column(name = "ruc", length = 20)
    private String ruc;
    
    @Column(name = "estado", nullable = false)
    private String estado = "ACTIVO";
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}