package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDetallePedido {

    private UUID idDetallePedido;

    private UUID idPedido;

    private UUID idVarianteProducto;

    private String nombreProducto;

    private BigDecimal precioUnitario;

    private Integer cantidad;

    private BigDecimal pagoSubtotal;

}
