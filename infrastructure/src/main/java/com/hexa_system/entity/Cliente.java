package com.hexa_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
