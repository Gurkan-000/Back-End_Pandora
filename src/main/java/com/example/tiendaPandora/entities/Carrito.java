package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Carritos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carrito {

    @Id
    @UuidGenerator
    private UUID idCarrito;

    @Column(precision = 10, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal precioTotal = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<DetalleCarrito> detalles = new ArrayList<>();

    public void addUsuario(Usuario usuario) {
        this.usuario = usuario;
        usuario.setCarrito(this);
    }

    public void recalcularPrecioTotal() {
        this.precioTotal = detalles.stream()
                .map(DetalleCarrito::getPrecioSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
