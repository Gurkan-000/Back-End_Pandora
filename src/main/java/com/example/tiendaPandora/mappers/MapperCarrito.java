package com.example.tiendaPandora.mappers;


import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.dtos.response.ResponseDetalleCarrito;
import com.example.tiendaPandora.entities.Carrito;

import java.util.List;

public class MapperCarrito {

    public static ResponseCarrito toResponse(Carrito carrito, List<ResponseDetalleCarrito> detalles) {
        return ResponseCarrito.builder()
                .idCarrito(carrito.getIdCarrito())
                .idUsuario(carrito.getUsuario().getIdUsuario())
                .cantidadProductos(carrito.getDetalles().size())
                .precioTotal(carrito.getPrecioTotal())
                .detallesCarrito(detalles)
                .build();
    }

    public static ResponseCarrito toResponse(Carrito carrito) {
        return ResponseCarrito.builder()
                .idCarrito(carrito.getIdCarrito())
                .idUsuario(carrito.getUsuario().getIdUsuario())
                .cantidadProductos(carrito.getDetalles().size())
                .precioTotal(carrito.getPrecioTotal())
                .build();
    }

}

