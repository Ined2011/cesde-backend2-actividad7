package com.example.demo_basic.model.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.demo_basic.model.enums.HistorialCrediticio;

@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cliente extends BaseEntity {

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "identificacion", nullable = false, length = 40, unique = true)
    private String identificacion;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "ingreso_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal ingresoMensual;

    @Column(name = "tiene_garantia", nullable = false)
    private Boolean tieneGarantia;

    @Enumerated(EnumType.STRING)
    @Column(name = "historial_crediticio", nullable = false, length = 20)
    private HistorialCrediticio historialCrediticio;
    // Valores: BUENO, MALO, SIN_HISTORIAL

    @Column(name = "capacidad_de_endeudamiento", nullable = false, precision = 12, scale = 2)

    @ColumnTransformer(write = "?::numeric(12,2)")
    private BigDecimal capacidadDeEndeudamiento;
}