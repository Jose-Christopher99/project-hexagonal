package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="comprobantes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comprobante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="id_venta", unique = true,nullable = false)
    private Venta venta;
    private String tipo;
    @Column(name = "nro_comprobante")
    private String numeroComprobante;
    @Column(name = "fecha")
    private LocalDate fechaEmision;
    @Column(name="hora")
    private LocalTime horaEmision;
}
