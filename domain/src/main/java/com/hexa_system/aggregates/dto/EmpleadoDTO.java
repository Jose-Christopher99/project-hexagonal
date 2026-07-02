package com.hexa_system.aggregates.dto;

public record EmpleadoDTO(
         String tipoDoc,
         String numDoc,
         String nombre,
         String apellidoP,
         String apellidoM,
         String telefono,
         String email,
         String password,
         String rol
) {
}
