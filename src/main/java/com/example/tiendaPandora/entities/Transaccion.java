package com.example.tiendaPandora.entities;

import com.example.tiendaPandora.entities.enums.EstadoTransaccion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Transacciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaccion {

    @Id
    @UuidGenerator
    private UUID idTransaccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estadoTransaccion;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fechaTransaccion;

    @Column(length = 255)
    private String referenciaPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_pedido",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transaccion_pedido")
    )
    private Pedido pedido;

}
