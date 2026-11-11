package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, UUID> {
}
