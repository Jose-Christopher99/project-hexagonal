package com.hexa_system.controller;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;
import com.hexa_system.aggregates.dto.VerificacionDTO;
import com.hexa_system.ports.in.AuthServiceIn;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Key;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final AuthServiceIn  authServiceIn;

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(
            @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authServiceIn.loginIn(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<SignInResponse> verificarUsuario(@RequestBody VerificacionDTO dto) {
        return ResponseEntity.ok(authServiceIn.verificarCodigoIn(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SignInResponse> refresh(
            @RequestHeader("refreshToken") String auth) {
        return ResponseEntity.ok(authServiceIn.refreshTokenIn(auth));
    }

    @GetMapping("/clave")
    public ResponseEntity<String> getClave(){
        Key key= Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String clave= Base64.getEncoder().encodeToString(key.getEncoded());
        return ResponseEntity.ok(clave);
    }
}
