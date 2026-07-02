package com.hexa_system.config;

import com.hexa_system.constants.Constants;
import com.hexa_system.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final EmpleadoRepository empleadoRepository;

    /*LA CONFIGURACION CENTRAL DE SEGURIDAD LO HACEMOS CON ESTE METODO, YA QUE DEFINIMOS QUE ENDPOINTS SON PUBLICOS
    LOS QUE REQUIEREN DE ROL, SI USAREMOS SESIONES O NO Y QUE FILTROS PERSONALIZADOS VAMOS A UTILIZAR.*/
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        //DESACTIVAMOS EL CROSSITE REQUEST FORGET
        httpSecurity.csrf(AbstractHttpConfigurer::disable)//lo desactivamos porque es una amenaza
                /*Definimos las rutas que vamos a manejar, que endpoints son publicos, que endpoints puede acceder
                el USER, que endpoints puede acceder el ADMIN y todos deben ser autenticados*/
                .authorizeHttpRequests(request ->
                        request.requestMatchers(Constants.PERMIT_ENDPOINTS).permitAll()//QUE ENDPOINTS SON PUBLICOS
                                .requestMatchers(Constants.ENDPOINTS_ADMIN).hasAnyAuthority("ADMIN")//ENDPOINTS QUE REQUIEREN ROL ADMIN
                                .requestMatchers(Constants.ENDPOINTS_VENDEDOR).hasAnyAuthority("VENDEDOR")//ENDPOINTS QUE REQUIEREN ROL VENDEDOR
                                .anyRequest().authenticated())
                /*.authorizeHttpRequests(request -> request
                        .anyRequest().permitAll())*/
                //DEFINIMOS SI VAMOS A USAR SESIONES O NO, EN ESTE CASO CON JWT NO LO HAREMOS
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //DEFINIMOS EL PROVEEDOR DE AUTENTICACION EL CUAL LO HEMOS DEFINIDO MEDIANTE UN METODO
                .authenticationProvider(authenticationProvider())
                /*ESTA LINEA ES PARA AGREGAR UN FILTRO ADICIONAL, JWT DEBE DE TENER SU PROPIO FILTRO, YA QUE
                PUEDE LLEGAR UN TOKEN VENCIDO. ESTE addFilterBefore ES PARA AGREGAR UN FILTRO ANTES DEL QUE YA
                TENEMOS DEFINIDO Y LLEGUE AL SPRING SECURITY*/
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    /*DEFINIMOS UN METODO DEL PROVEEDOR DE LA AUTENTICACION*/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(
                username -> empleadoRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado"))
        );
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    //EL METODO QUE VAMOS A UTILIZAR PARA DEFINIR DE QUE FORMA LA VAMOS A ENCRIPTAR NUESTRAS CONTRASEÑAS
    //SOLO RETORNA UNA INSTANCIA DE BCRYPTPASSWORDENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //ES EL ORQUESTADOR QUE RECIBE UNA AUTENTICACION Y LO DELEGA AL PROVEEDOR DE AUTENTICACION ADECUADO
    //CUANDO HACEMOS UN LOGIN CON USER Y PASSWORD LO HACEMOS CON EL AUTHENTICATIONMANAGER POR MEDIO DEL PROVEEDOR DE AUTENTICACION
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }
}
