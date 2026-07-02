package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="orden_ventas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="id_cliente", nullable=false)
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name="id_empleado",nullable=false)
    private Empleado empleado;
    private LocalDate fecha_venta;
    private LocalTime hora_venta;
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    private String estadoPago;
}
