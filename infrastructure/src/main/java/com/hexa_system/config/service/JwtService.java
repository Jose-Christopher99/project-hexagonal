package com.hexa_system.config.service;

import com.hexa_system.entity.Empleado;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;

/*VAMOS A CREAR LOS METODOS PARA TODOS LOS METODOS RELACIONADOS CON LOS TOKENS
* PARA SACAR UN TOKEN, PARA UN ACCESS TOKEN, VALIDAR UN TOKEN, REFRESCAR EL TOKEN, PARA VALIDAR SI ES UN REFRESCAMIENTO
DE TOKEN O TOKEN ORIGINAL*/
public interface JwtService {
    //SE ENCARGARA UNICAMENTE DE EXTRAER EL NOMBRE DE USUARIO
    String extractUserName(String token);

    //SE ENCARGARA DE GENERAR EL TOKEN
    String generateToken(Empleado empleado);

    //SE ENCARGARA DE VALIDAR EL TOKEN
    boolean validateToken(String token, UserDetails userDetails);

    //SE ENCARGA DE GENERAR EL REFRESCO DE LOS TOKENS
    String generateRefreshToken(Map<String,Object> extraClaims, UserDetails userDetails);

    //SE ENCARGARA DE VALIDAR EL TIPO DE TOKEN
    boolean validateIsRefreshToken(String token);
}
