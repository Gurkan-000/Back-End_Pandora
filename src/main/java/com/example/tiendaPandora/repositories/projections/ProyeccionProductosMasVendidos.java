package com.example.tiendaPandora.repositories.projections;


import java.math.BigDecimal;
import java.util.UUID;

public interface ProyeccionProductosMasVendidos {

    UUID getIdProducto();

    String getNombreProducto();

    String getUrlImagen();

    Long getUnidadesVendidas();

    BigDecimal getImporteTotal();
}

