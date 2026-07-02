package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idRol;
    @Column(name="nombre_rol", unique=true, nullable=false)
    private String nombreRol;
}
