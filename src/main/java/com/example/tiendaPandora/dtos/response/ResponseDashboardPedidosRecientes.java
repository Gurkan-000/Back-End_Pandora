package com.example.tiendaPandora.dtos.response;

import com.example.tiendaPandora.entities.enums.EstadoPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDashboardPedidosRecientes {

    private UUID idPedido;
    private String correo;
    private EstadoPedido estadoPedido;
    private LocalDateTime fechaPedido;
    private BigDecimal pagoTotal;
}

