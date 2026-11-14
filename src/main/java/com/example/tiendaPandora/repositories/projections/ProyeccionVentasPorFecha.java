package com.example.tiendaPandora.repositories.projections;


import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProyeccionVentasPorFecha {

    LocalDate getFecha();

    BigDecimal getVentas();

    Long getCantidadPedidos();

}
