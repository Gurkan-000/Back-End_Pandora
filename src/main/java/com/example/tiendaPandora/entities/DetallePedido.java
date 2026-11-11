package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "Detalles_Pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedido {

    @Id
    @UuidGenerator
    private UUID idDetallePedido;

    @Column(length = 100, nullable = false)
    private String nombreProducto;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal pagoSubtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_pedido",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_detallepedido_pedido")
    )
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_variante_producto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_detallepedido_variante")
    )
    private VarianteProducto varianteProducto;

    public void calcularSubtotal() {
        this.pagoSubtotal = precioUnitario.multiply(
                BigDecimal.valueOf(cantidad)
        );
    }

}
