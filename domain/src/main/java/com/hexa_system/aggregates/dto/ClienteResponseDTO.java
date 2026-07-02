package com.hexa_system.aggregates.dto;

public record ClienteResponseDTO(
        String first_name,
        String first_last_name,
        String second_last_name,
        String document_number
) {
}
