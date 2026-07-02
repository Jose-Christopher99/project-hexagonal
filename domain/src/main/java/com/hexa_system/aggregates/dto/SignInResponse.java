package com.hexa_system.aggregates.dto;

public record SignInResponse(
        String accessToken,
        String refreshToken
) {
}
