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
    private BigDecimal precioUnitario;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precioSubTotal;

    @ManyToOne
    @JoinColumn(name = "id_carrito", foreignKey = @ForeignKey(name = "fk_detallecarrito_carrito"))
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_variante_producto", foreignKey = @ForeignKey(name = "fk_varianteproducto_detallecarrito"))
    private VarianteProducto varianteProducto;

    public void addCarrito(Carrito carrito){
        this.carrito = carrito;
        carrito.getDetalles().add(this);
    }

    public void addVarianteProducto(VarianteProducto varianteProducto){
        this.varianteProducto = varianteProducto;
    }

    public void calcularSubTotal(BigDecimal precioUnitario){
        this.precioUnitario = precioUnitario;
        precioSubTotal = this.precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

}
