package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Valores_atributo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValorAtributo {

    @Id
    @UuidGenerator
    private UUID idValorAtributo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_atributo",
            foreignKey = @ForeignKey(name = "fk_valor_atributo")
    )
    private Atributo atributo;

    @Column(length = 30, nullable = false)
    private String valor;

    @OneToMany(
            mappedBy = "valor",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @Builder.Default
    private List<VarianteAtributo> variantesAtributo = new ArrayList<>();

    public void addVarianteAtributo(VarianteAtributo variante){
        variantesAtributo.add(variante);
        variante.setValor(this);
    }

}