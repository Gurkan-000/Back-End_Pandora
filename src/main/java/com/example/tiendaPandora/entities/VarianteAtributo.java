package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Variante_atributos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VarianteAtributo {

    @EmbeddedId
    private VarianteAtributoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idVariante")
    @JoinColumn(
            name = "id_variante",
            foreignKey = @ForeignKey(name = "fk_variante_atributo_variante")
    )
    private VarianteProducto variante;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idValor")
    @JoinColumn(
            name = "id_valor",
            foreignKey = @ForeignKey(name = "fk_variante_atributo_valor")
    )
    private ValorAtributo valor;

}