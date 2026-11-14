package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDashboardProductosMasVendidos {

    private UUID idProducto;
    private String nombreProducto;
    private String urlImagen;
    private Long unidadesVendidas;
    private BigDecimal importeTotal;
}
