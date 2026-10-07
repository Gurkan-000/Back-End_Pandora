package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Variantes_producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VarianteProducto {

    @Id
    @UuidGenerator
    private UUID idVarianteProducto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_producto",
            foreignKey = @ForeignKey(name = "fk_variante_producto")
    )
    private Producto producto;

    @Column(nullable = false)
    private Integer stock;

    @Column(
            name = "precio",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal precio;

    @Column(nullable = false)
    private String url;

    @OneToMany(
            mappedBy = "varianteProducto",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ImagenProducto> imagenes = new ArrayList<>();

    @OneToMany(
            mappedBy = "variante",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<VarianteAtributo> variantesAtributos = new ArrayList<>();

    public void addVarianteAtributo(VarianteAtributo varianteAtributo){
        variantesAtributos.add(varianteAtributo);
        varianteAtributo.setVariante(this);
    }

}
