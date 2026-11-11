package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Ventas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Venta {

    @Id
    @UuidGenerator
    private UUID idVenta;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_pedido",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_venta_pedido")
    )
    private Pedido pedido;

}
