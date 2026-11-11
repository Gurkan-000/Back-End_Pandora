package com.example.tiendaPandora.entities;

import com.example.tiendaPandora.entities.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pedido {

    @Id
    @UuidGenerator
    private UUID idPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @Column(length = 255, nullable = false)
    private String direccionEntrega;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal pagoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pedido_usuario")
    )
    private Usuario usuario;

    @OneToMany(
            mappedBy = "pedido",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    @OneToMany(
            mappedBy = "pedido",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Transaccion> transacciones = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private Venta venta;

    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }

    public void addTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
        transaccion.setPedido(this);
    }

    public void addUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
