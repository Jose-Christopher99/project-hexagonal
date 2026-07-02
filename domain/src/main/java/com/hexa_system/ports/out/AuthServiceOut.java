package com.hexa_system.ports.out;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;
import com.hexa_system.aggregates.dto.VerificacionDTO;

public interface AuthServiceOut {
    SignInResponse loginOut(SignInRequest request);
    SignInResponse verificarCodigoOut(VerificacionDTO dto);
    SignInResponse refreshTokenOut(String refreshToken);
}
