package com.hexa_system.aggregates.dto;

public record ClienteDTO(
        Long id,
        String tipoDoc,
        String numDoc,
        String nombre,
        String apellidoP,
        String apellidoM,
        String telefono
) {
}
