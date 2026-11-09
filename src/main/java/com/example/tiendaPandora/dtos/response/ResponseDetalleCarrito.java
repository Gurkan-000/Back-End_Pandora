package com.example.tiendaPandora.dtos.response;


import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDetalleCarrito {

    private UUID idDetalleCarrito;

    private UUID idCarrito;

    private UUID idVarianteProducto;

    private Integer cantidad;

    private BigDecimal precioSubTotal;

}

