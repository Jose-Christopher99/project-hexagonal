package com.hexa_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class Usuario {
    @Column(nullable = false,name="tipo_doc",length =10)
    private String tipoDoc;
    @Column(nullable = false,name="num_doc",length =12)
    private String numDoc;
    @Column(nullable = false,name="nombres")
    private String nombre;
    @Column(nullable = false,name="apellidoP")
    private String apellidoP;
    @Column(nullable = false,name="apellidoM")
    private String apellidoM;
    @Column(nullable = false,name="telefono")
    private String telefono;
}
