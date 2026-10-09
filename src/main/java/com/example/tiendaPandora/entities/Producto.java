package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Productos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto {

    @Id
    @UuidGenerator
    private UUID idProducto;


    @Column(length = 60, nullable = false, unique = true)
    private String nombreProducto;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(
            name = "precio",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal precio;

    @Column(nullable = false)
    private String urlImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_marca",
            foreignKey = @ForeignKey(name = "fk_producto_marca")
    )
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_subcategoria",
            foreignKey = @ForeignKey(name = "fk_producto_subcategoria")
    )
    private SubCategoria subCategoria;

    @OneToMany(
            mappedBy = "producto",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ProductoAtributo> productosAtributos = new ArrayList<>();

    @OneToMany(
            mappedBy = "producto",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<VarianteProducto> variantes = new ArrayList<>();

    public void addProductoAtributo(ProductoAtributo productoAtributo) {
        productosAtributos.add(productoAtributo);
        productoAtributo.setProducto(this);
    }

    public void addVariante(VarianteProducto variante) {
        variantes.add(variante);
        variante.setProducto(this);
    }

}