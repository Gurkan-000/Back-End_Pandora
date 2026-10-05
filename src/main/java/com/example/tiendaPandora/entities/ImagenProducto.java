package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "Imagenes_producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagenProducto {

    @Id
    @UuidGenerator
    private UUID idImagen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_variante_producto",
            foreignKey = @ForeignKey(name = "fk_imagen_variante")
    )
    private VarianteProducto varianteProducto;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    @Builder.Default
    private Boolean esPrincipal = false;

}
