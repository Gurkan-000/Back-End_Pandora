package com.example.tiendaPandora.repositories.projections;


import java.math.BigDecimal;

public interface ProyeccionResumenVentas {

    BigDecimal getVentasTotales();

    Long getCantidadPedidos();

    BigDecimal getTicketPromedio();

}

