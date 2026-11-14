package com.example.tiendaPandora.repositories.projections;


import com.example.tiendaPandora.entities.enums.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ProyeccionPedidosRecientes {

    UUID getIdPedido();

    String getCorreo();

    EstadoPedido getEstadoPedido();

    LocalDateTime getFechaPedido();

    BigDecimal getPagoTotal();
}
