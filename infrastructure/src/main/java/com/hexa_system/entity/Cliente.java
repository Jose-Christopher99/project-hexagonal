package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = "clientes")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
