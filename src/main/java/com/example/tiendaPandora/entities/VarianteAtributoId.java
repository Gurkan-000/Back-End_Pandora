package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VarianteAtributoId implements Serializable {

    @Column(nullable = false)
    private UUID idVariante;

    @Column(nullable = false)
    private UUID idValor;

}