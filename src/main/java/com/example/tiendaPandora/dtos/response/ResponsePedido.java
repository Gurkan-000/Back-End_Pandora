package com.example.tiendaPandora.dtos.response;

import com.example.tiendaPandora.entities.enums.EstadoPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePedido {

    private UUID idPedido;

    private UUID idUsuario;

    private UUID idVenta;

    private EstadoPedido estadoPedido;

    @JsonFormat(pattern = "dd-MM-yy HH:mm")
    private LocalDateTime fechaPedido;

    private BigDecimal pagoTotal;

}