package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Subcategorias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoria {

    @Id
    @UuidGenerator
    private UUID idSubcategoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_categoria",
            foreignKey = @ForeignKey(name = "fk_subcategoria_categoria")
    )
    private Categoria categoria;

    @Column(length = 40, nullable = false, unique = true)
    private String nombreSubcategoria;

    @OneToMany(
            mappedBy = "subCategoria",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    public void addProducto(Producto producto){
        productos.add(producto);
        producto.setSubCategoria(this);
    }

}
