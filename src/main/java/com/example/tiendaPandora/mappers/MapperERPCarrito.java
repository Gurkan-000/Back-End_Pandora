package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.dtos.response.ResponseERPCarrito;
import com.example.tiendaPandora.entities.Carrito;

import java.util.List;

public class MapperERPCarrito {

    public static ResponseERPCarrito toResponse(Carrito carrito) {
        return ResponseERPCarrito.builder()
                .idCarrito(carrito.getIdCarrito())
                .idUsuario(carrito.getUsuario().getIdUsuario())
                .correo(carrito.getUsuario().getCorreo())
                .cantidadProductos(carrito.getDetalles().size())
                .precioTotal(carrito.getPrecioTotal())
                .build();
    }

}
