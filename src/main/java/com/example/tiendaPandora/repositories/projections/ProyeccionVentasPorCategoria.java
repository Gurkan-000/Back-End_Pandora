package com.example.tiendaPandora.repositories.projections;


import java.math.BigDecimal;
import java.util.UUID;

public interface ProyeccionVentasPorCategoria {

    UUID getIdCategoria();

    String getNombreCategoria();

    BigDecimal getVentasTotales();

    Long getUnidadesVendidas();
}

