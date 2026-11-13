package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseERPCarrito {

    private UUID idCarrito;

    private UUID idUsuario;

    private String correo;

    private Integer cantidadProductos;

    private BigDecimal precioTotal;

}
