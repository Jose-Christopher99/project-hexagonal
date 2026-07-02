package com.hexa_system.adapters;

import com.hexa_system.aggregates.dto.SignInRequest;
import com.hexa_system.aggregates.dto.SignInResponse;
import com.hexa_system.config.service.JwtService;
import com.hexa_system.entity.Empleado;
import com.hexa_system.ports.out.AuthServiceOut;
import com.hexa_system.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthAdapterOut implements AuthServiceOut {
    private final EmpleadoRepository empleadoRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public SignInResponse loginOut(SignInRequest request) {
        //DELEGAMOS LA AUTENTICACION CON USUARIO Y CONTRASEÑA UTILIZAMOS AL AUTHENTICATIONMANAGER
        //AUTENTICACION CON LAS CREDENCIALES (email y password)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                request.password()));
        //BUSCAR AL EMPLEADO EN LA BASE DE DATOS
        Empleado empleado= empleadoRepository.findByEmail(request.email())
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado en la base de datos"));
        //GENERAR EL TOKEN
        String accessToken= jwtService.generateToken(empleado);
        String refreshToken= jwtService.generateRefreshToken(new HashMap<>(), empleado);
        //DEVUELVO LA RESPUESTA
        return  new SignInResponse(accessToken,refreshToken);
    }

    @Override
    public SignInResponse refreshTokenOut(String refreshToken) {
        //PARA GENERAR UN NUEVO ACCESS TOKEN A PARTIR DE UN REFRESH TOKEN
        //PASO 1: DEBEMOS ASEGURARNOS DE QUE EL TOKEN QUE NOS MANDAN SEA UN REFRESH TOKEN
        if(!jwtService.validateIsRefreshToken(refreshToken)){
            throw new RuntimeException("ERROR: Token ingresado no es de tipo REFRESH");
        }
        //PASO 2: EXTRAER EL USERNAME DEL TOKEN
        String userEmail= jwtService.extractUserName(refreshToken);
        //PASO 3: CARGAR AL USUARIO DE BASE DE DATOS
        Empleado empleado= empleadoRepository.findByEmail(userEmail).
                orElseThrow(()-> new UsernameNotFoundException("El usuario no se encuentra en la base de datos"));
        //AHORA VALIDAMOS SI EL TOKEN AUN ESTA VIGENTE
        if(!jwtService.validateToken(refreshToken,empleado)){
            throw new IllegalStateException("ERROR: Token ingresado no es VALIDO o esta VENCIDO");
        }
        //GENERAMOS EL NUEVO ACCESS TOKEN
        String newAccessToken = jwtService.generateToken(empleado);
        //RETORNAMOS NUEVAMENTE LOS TOKENS TANTO COMO EL NUEVO ACCESS TOKEN Y EL REFRESH TOKEN QUE YA TENIAMOS
        return new SignInResponse(newAccessToken,refreshToken);
    }
}
