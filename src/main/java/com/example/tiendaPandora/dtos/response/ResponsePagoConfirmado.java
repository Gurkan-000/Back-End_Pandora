package com.example.tiendaPandora.dtos.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponsePagoConfirmado(
        UUID idPedido,
        String estadoPago,
        BigDecimal monto
) {}