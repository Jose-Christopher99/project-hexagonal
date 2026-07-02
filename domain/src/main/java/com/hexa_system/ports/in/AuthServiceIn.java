package com.hexa_system.ports.in;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;
import com.hexa_system.aggregates.dto.VerificacionDTO;

public interface AuthServiceIn {
    SignInResponse loginIn(SignInRequest request);
    SignInResponse verificarCodigoIn(VerificacionDTO dto);
    SignInResponse refreshTokenIn(String refreshToken);
}
