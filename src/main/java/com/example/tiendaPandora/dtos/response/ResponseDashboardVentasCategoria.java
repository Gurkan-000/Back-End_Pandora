package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDashboardVentasCategoria {

    private UUID idCategoria;
    private String nombreCategoria;
    private BigDecimal ventasTotales;
    private Long unidadesVendidas;
}

