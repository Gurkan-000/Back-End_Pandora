package com.example.tiendaPandora.dtos.response;

import java.util.UUID;

public record ResponseInicioPago(
        UUID idPedido,
        String idOrdenPayPal,
        String monto,
        String moneda
) {}