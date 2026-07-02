package com.hexa_system.aggregates.dto;

public record EmpleadoResponseDTO(
        Long id,
        String tipoDoc,
        String numDoc,
        String nombre,
        String apellidoP,
        String apellidoM,
        String telefono,
        String email,
        String rol
) {
}
