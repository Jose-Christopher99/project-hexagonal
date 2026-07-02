package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="codigos_verificacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodigoVerificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(length = 6)
    private String codigo;
    private LocalDateTime expiracion;
    private Boolean usado=false;
}
