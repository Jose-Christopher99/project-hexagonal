package com.hexa_system.aggregates.dto;

public record SignInRequest(
        String email,
        String password
) {
}
