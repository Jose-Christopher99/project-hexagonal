package com.hexa_system.config;

import com.hexa_system.config.service.JwtService;
import com.hexa_system.repository.EmpleadoRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final EmpleadoRepository empleadoRepository;
    /*Vamos a utilizar todo este filtro de JWT para analizar cuando nos venga una solicitud
    de manera exclusiva con token.
    Este filtro se ejecutara antes del filtro de Spring Security*/
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        //Primero vamos a capturar la peticion y sacaremos el token del Authorization
        final String tokenExtraidoHeader= request.getHeader("Authorization");
        //Cuando enviamos un token por una solicitud siempre va con un Bearer para ello hacemos una pequeña validacion
        // El header authorization no existe, esta vacio o solo tiene espacio || el token no inicia con Bearer
        if(!StringUtils.hasText(tokenExtraidoHeader) || !tokenExtraidoHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //Con el substring pasamos de esto: Bearer xaxixhadohias -> a esto xaxixhadohias
        String token= tokenExtraidoHeader.substring(7); //aqui eliminamos el bearer
        String username= jwtService.extractUserName(token);//Lo que se hace es extraer el nombre del usuario del token

        //Validaciones antes de volver a autenticar
        /*PREGUNTO SI NO HAY UNA AUTENTICACION CARGADA EN EL CONTEXTO, PARA EVITAR REAUTENTICAR UN REQUEST
        QUE YA FUE AUTORIZADO O QUE YA FUE AUTENTICADO POR ALGUN OTRO FILTRO O MECANISMO PARA YA NO VOLVER A CONSTRUIRLO*/
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            //CARGAMOS LOS DETALLES DEL USUARIO DE LA BASE DE DATOS
            UserDetails userDetails= empleadoRepository.findByEmail(username)
                    .orElseThrow(
                            ()-> new RuntimeException("Empleado no encontrado en la base de datos"));
            //AHORA VALIDAMOS SI EL TOKEN ES VALIDO Y NO ES UN REFRESH TOKEN
            if(jwtService.validateToken(token,userDetails) && !jwtService.validateIsRefreshToken(token)){
                /*AHORA CREAMOS UN OBJETO DE AUTENTICACION EL CUAL SPRING SECURITY LO VA A GUARDAR COMO USUARIO AUTENTICADO
                VA NULO PORQUE YA NO ESTAMOS AUTENTICANDO COMO PASSWORD, SI NO COMO UN TOKEN Y EL GETAUTHORITIES REPRESENTA
                LOS ROLES O PERMISOS QUE EL USUARIO TIENE*/
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                //AÑADIMOS LOS DETALLES DE LA SOLITUD PUEDE SER IP DEL CLIENTE, DISPOSITIVO, NAVEGADOR PERO AL OBJETO DE AUTENTICACION
                //TODA LA INFORMACION ADICIONAL DEL REQUEST LO AGREGAMOS AL OBJETO AUTENTICADO
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //CREAMOS EL CONTEXTO DE SEGURIDAD VACIO SIN NADA, PORQUE AQUI QUEREMOS COLOCAR MI OBJETO AUTENTICADO
                SecurityContext context= SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                //ASIGNAMOS ESE CONTEXTO AL SECURITYCONTEXTHOLDER
                SecurityContextHolder.setContext(context);
            }
        }
        //FINALMENTE SI YA SE LOGUEO CON TOKEN, DEBERIAMOS DEJAR PASAR LA PETICION
        ///YA TERMINE MI TRABAJO, DEJA QUE LA PETICION SIGA SU CAMINO
        filterChain.doFilter(request,response);
    }
}
