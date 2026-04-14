package com.example.demo_basic.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.demo_basic.model.enums.EstadoCuota;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cuotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cuota extends BaseEntity {

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota;

    @Column(name = "valor_cuota", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorCuota;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoCuota estadoPago;
    // Valores: PENDIENTE, PAGADA, EN_MORA

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prestamo_id", nullable = false)
    private Prestamo prestamo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
