package com.hexa_system.aggregates.dto;

import java.util.List;

public record ProcesarVentaDTO(
        Long empleadoId,
        String tipoComprobante,
        ClienteDTO cliente,
        List<DetalleVentaDTO> productos
) {
}
