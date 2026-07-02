package com.hexa_system.controller;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;
import com.hexa_system.ports.in.AuthServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/refresh")
    public ResponseEntity<SignInResponse> refresh(
            @RequestParam String refreshToken) {
        return ResponseEntity.ok(authServiceIn.refreshTokenIn(refreshToken));
    }
}
