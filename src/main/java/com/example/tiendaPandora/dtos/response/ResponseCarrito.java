package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseCarrito {

    private UUID idCarrito;

    private UUID idUsuario;

    private BigDecimal precioTotal;

}

