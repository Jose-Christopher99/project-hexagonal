package com.hexa_system.aggregates.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record VentaDTO(
        Long id,
        String nombreCliente,
        BigDecimal total,
        String estado,
        String numeroComprobante,
        String tipoComprobante,
        LocalDate fechaEmision,
        LocalTime horaEmision
) {
}
