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
public class ResponseDashboardVarianteStock {

    private UUID idVarianteProducto;

    private UUID idProducto;

    private String nombreProducto;

    private String atributos;

    private Integer stock;

    private String estado;

}
