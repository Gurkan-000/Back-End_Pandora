package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Pedido;
import com.example.tiendaPandora.repositories.projections.ProyeccionPedidosRecientes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByUsuario_CorreoOrderByFechaPedidoDesc(String correo);

    @Query("""
        SELECT
            p.idPedido AS idPedido,
            u.correo AS correo,
            p.estadoPedido AS estadoPedido,
            p.fechaPedido AS fechaPedido,
            p.pagoTotal AS pagoTotal
        FROM Pedido p
        JOIN p.usuario u
        ORDER BY p.fechaPedido DESC
    """)
    List<ProyeccionPedidosRecientes> obtenerPedidosRecientes(Pageable pageable);

}
