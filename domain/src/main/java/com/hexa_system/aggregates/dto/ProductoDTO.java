package com.hexa_system.aggregates.dto;

import java.math.BigDecimal;

public record ProductoDTO(
        Long id,
        String nombre,
        BigDecimal precio,
        Integer stock,
        String categoria
) {
}
