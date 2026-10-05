package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Categorias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categoria {

    @Id
    @UuidGenerator
    private UUID idCategoria;

    @Column(length = 40, nullable = false, unique = true)
    private String nombreCategoria;

    @OneToMany(
            mappedBy = "categoria",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<SubCategoria> subCategorias = new ArrayList<>();

    public void addSubCategoria(SubCategoria subCategoria) {
        subCategorias.add(subCategoria);
        subCategoria.setCategoria(this);
    }

}
