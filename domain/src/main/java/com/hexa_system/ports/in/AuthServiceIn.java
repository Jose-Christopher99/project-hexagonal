package com.hexa_system.ports.in;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;

public interface AuthServiceIn {
    SignInResponse loginIn(SignInRequest request);
    SignInResponse refreshTokenIn(String refreshToken);
}
