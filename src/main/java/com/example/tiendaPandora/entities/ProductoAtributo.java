package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Producto_atributos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoAtributo {

    @EmbeddedId
    private ProductoAtributoId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idProducto")
    @JoinColumn(
            name = "id_producto",
            foreignKey = @ForeignKey(name = "fk_producto_atributo_producto")
    )
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idAtributo")
    @JoinColumn(
            name = "id_atributo",
            foreignKey = @ForeignKey(name = "fk_producto_atributo_atributo")
    )
    private Atributo atributo;

}
