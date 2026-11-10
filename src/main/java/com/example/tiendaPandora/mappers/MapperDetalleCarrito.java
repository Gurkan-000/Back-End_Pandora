package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestDetalleCarrito;
import com.example.tiendaPandora.dtos.response.ResponseDetalleCarrito;
import com.example.tiendaPandora.entities.DetalleCarrito;

public class MapperDetalleCarrito {

    public static DetalleCarrito toEntity(RequestDetalleCarrito requestDetalleCarrito) {
        return DetalleCarrito.builder()
                .cantidad(requestDetalleCarrito.getCantidad())
                .build();
    }

    public static ResponseDetalleCarrito toResponse(DetalleCarrito detalleCarrito) {
        return ResponseDetalleCarrito.builder()
                .idDetalleCarrito(detalleCarrito.getIdDetalleCarrito())
                .idCarrito(detalleCarrito.getCarrito().getIdCarrito())
                .idVarianteProducto(detalleCarrito.getVarianteProducto().getIdVarianteProducto())
                .precioUnitario(detalleCarrito.getPrecioUnitario())
                .cantidad(detalleCarrito.getCantidad())
                .precioSubTotal(detalleCarrito.getPrecioSubTotal())
                .build();
    }
}

