package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByUsuario_CorreoOrderByFechaPedidoDesc(String correo);

}
