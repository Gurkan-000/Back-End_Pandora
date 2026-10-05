package com.example.tiendaPandora.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoAtributoId implements Serializable {

    @Column(nullable = false)
    private UUID idProducto;

    @Column(nullable = false)
    private UUID idAtributo;

}