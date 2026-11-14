package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDashboardVentas {

    private LocalDate fecha;
    private BigDecimal ventas;
    private Long cantidadPedidos;

}

