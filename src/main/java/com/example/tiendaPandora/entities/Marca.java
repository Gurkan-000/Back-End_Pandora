package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Marcas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Marca {

    @Id
    @UuidGenerator
    private UUID idMarca;

    @Column(length = 40, nullable = false, unique = true)
    private String nombreMarca;

    @OneToMany(
            mappedBy = "marca",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();

    public void addProducto(Producto producto){
        productos.add(producto);
        producto.setMarca(this);
    }

}
