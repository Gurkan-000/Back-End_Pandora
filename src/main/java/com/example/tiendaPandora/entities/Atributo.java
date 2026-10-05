package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "atributos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Atributo {

    @Id
    @UuidGenerator
    private UUID idAtributo;

    @Column(length = 40, nullable = false)
    private String nombreAtributo;

    @OneToMany(
            mappedBy = "atributo",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ValorAtributo> valores = new ArrayList<>();

    public void addValorAtributo(ValorAtributo valorAtributo){
        valores.add(valorAtributo);
        valorAtributo.setAtributo(this);
    }

    public void addProductoAtributo(ProductoAtributo productoAtributo){
        productoAtributo.setAtributo(this);
    }

}
