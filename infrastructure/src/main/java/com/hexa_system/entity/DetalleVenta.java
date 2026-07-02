package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name="detalle_venta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;
    @ManyToOne
    @JoinColumn(name="orden_id", nullable=false)
    private Venta venta;
    @ManyToOne
    @JoinColumn(name="id_producto",nullable=false)
    private Producto productos;
    private Integer cantidad;
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;
}
