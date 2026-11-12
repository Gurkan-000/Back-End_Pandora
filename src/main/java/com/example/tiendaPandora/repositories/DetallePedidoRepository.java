package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.DetallePedido;
import com.example.tiendaPandora.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, UUID> {

    List<DetallePedido> findByPedido_IdPedido(UUID idPedido);

}
