package com.hexa_system.usecase;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;
import com.hexa_system.ports.in.AuthServiceIn;
import com.hexa_system.ports.out.AuthServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServiceIn {
    private final AuthServiceOut authServiceOut;

    @Override
    public SignInResponse loginIn(SignInRequest request) {
        SignInResponse response = authServiceOut.loginOut(request);
        return response;
    }

    @Override
    public SignInResponse refreshTokenIn(String refreshToken) {
        SignInResponse response = authServiceOut.refreshTokenOut(refreshToken);
        return response;
    }
}
