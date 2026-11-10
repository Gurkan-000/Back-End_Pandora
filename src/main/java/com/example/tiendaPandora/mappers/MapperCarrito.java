package com.example.tiendaPandora.mappers;


import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.entities.Carrito;

public class MapperCarrito {

    public static ResponseCarrito toResponse(Carrito carrito) {
        return ResponseCarrito.builder()
                .idCarrito(carrito.getIdCarrito())
                .idUsuario(carrito.getUsuario().getIdUsuario())
                .cantidadProductos(carrito.getDetalles().size())
                .precioTotal(carrito.getPrecioTotal())
                .build();
    }
}

