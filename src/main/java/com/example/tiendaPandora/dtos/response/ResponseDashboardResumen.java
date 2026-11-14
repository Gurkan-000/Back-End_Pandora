package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDashboardResumen {

    private BigDecimal ventasTotales;
    private Long cantidadPedidos;
    private Long unidadesVendidas;
    private BigDecimal ticketPromedio;

    private BigDecimal variacionVentas;
    private BigDecimal variacionPedidos;
    private BigDecimal variacionUnidades;
    private BigDecimal variacionTicketPromedio;

}

