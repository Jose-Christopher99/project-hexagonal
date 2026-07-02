package com.hexa_system.ports.out;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;

public interface AuthServiceOut {
    SignInResponse loginOut(SignInRequest request);
    SignInResponse refreshTokenOut(String refreshToken);
}
