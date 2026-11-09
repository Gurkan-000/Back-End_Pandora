package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "Detalles_Carrito")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleCarrito {

    @Id
    @UuidGenerator
    private UUID idDetalleCarrito;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio_unitario;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precioSubTotal;

    @ManyToOne
    @JoinColumn(name = "id_carrito", foreignKey = @ForeignKey(name = "fk_detallecarrito_carrito"))
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_variante_producto", foreignKey = @ForeignKey(name = "fk_varianteproducto_detallecarrito"))
    private VarianteProducto varianteProducto;

}
