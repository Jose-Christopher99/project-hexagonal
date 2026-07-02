package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="empleados")
@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Empleado extends Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", unique = true,nullable = false)
    private String email;
    @Column(name="password",nullable = false)
    private String password;
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    public Empleado(Long id, String email, String password, Rol rol,
                    String tipoDoc, String numDoc, String nombre,
                    String apellidoP, String apellidoM, String telefono){
        super(tipoDoc, numDoc, nombre, apellidoP, apellidoM, telefono);
        this.id = id;
        this.email = email;
        this.password = password;
        this.rol=rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.getNombreRol()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
