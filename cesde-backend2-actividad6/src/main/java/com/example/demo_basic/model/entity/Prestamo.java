package com.example.demo_basic.model.entity;

import com.example.demo_basic.model.enums.EstadoPrestamo;
import com.example.demo_basic.model.enums.CapacidadPrestamo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prestamos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo extends BaseEntity {

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "especie", nullable = false, length = 40)
    private String especie;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamano", nullable = false, length = 20)
    private CapacidadPrestamo tamano;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPrestamo estado;
}
