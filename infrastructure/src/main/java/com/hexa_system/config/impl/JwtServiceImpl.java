package com.hexa_system.config.impl;

import com.hexa_system.config.service.JwtService;
import com.hexa_system.entity.Empleado;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtServiceImpl implements JwtService {
    @Value("${key.signature}")
    private String keySignature;

    @Override
    public String extractUserName(String token) {
        //Necesita el token y una funcion y necesitamos un Subject del Claims del token
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(Empleado empleado) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "ACCESS");
        claims.put("id", empleado.getId());
        claims.put("nombre", empleado.getNombre());
        claims.put("apellido", empleado.getApellidoP());
        claims.put("rol", empleado.getRol().getNombreRol());
        claims.put("isAccountNonExpired", true);
        claims.put("isAccountNonLocked", true);
        claims.put("isCredentialsNonExpired", true);
        claims.put("isEnabled", true);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 60000 * 30); // 30 minutos

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(empleado.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username= extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims != null ? extraClaims: new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+600000))
                .claim("type","refresh")
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    //ESTE METODO VALIDA QUE EL TOKEN QUE SE RECIBE SEA UN RESFRESH TOKEN O NO
    @Override
    public boolean validateIsRefreshToken(String token) {
        //EXTRAEMOS LOS CLAIMS
        Claims claims = extractAllClaims(token);
        //DE LOS CLAIMS EXTRAEMOS EL TYPE Y LO CONVERTIMOS A UN STRING
        String typeToken= claims.get("type",String.class);
        return "refresh".equalsIgnoreCase(typeToken);
    }

    //METODO DONDE PODAMOS RETORNAR UNA LLAVE YA LISTA PARA USAR CON EL TOKEN
    private Key getSignKey(){
        byte[] key = Decoders.BASE64.decode(keySignature);
        return Keys.hmacShaKeyFor(key);
    }

    //METODO PARA EXTRAER TODOS LOS CLAIMS DEL PAYLOAD
    private Claims extractAllClaims(String token){
        //AQUI PARSEAMOS TODOS LOS OBJETOS DEL TOKEN DEL PAYLOAD EN UN OBJETO CLAIMS
        return Jwts.parserBuilder()
                //PRIMERO DEBEMOS VERIFICAR EL TOKEN
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();//Aqui especificamos que queremos el Payload el cuerpo del token
    }

    //METODO PARA EXTRAER UN SOLO CLAIM EN ESPECIFICO DEL PAYLOAD
    private <T> T extractClaim(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(extractAllClaims(token));
    }

    //METODO PARA VALIDAR SI MI TOKEN HA EXPIRADO O NO HA EXPIRADO
    private boolean isTokenExpired (String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
