package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.response.ResponseDetallePedido;
import com.example.tiendaPandora.entities.DetallePedido;
import com.example.tiendaPandora.entities.Pedido;

public class MapperDetallePedido {

    public static ResponseDetallePedido toResponse(DetallePedido detallePedido) {
        return ResponseDetallePedido.builder()
                .idDetallePedido(detallePedido.getIdDetallePedido())
                .idPedido(detallePedido.getPedido().getIdPedido())
                .idVarianteProducto(detallePedido.getVarianteProducto().getIdVarianteProducto())
                .nombreProducto(detallePedido.getNombreProducto())
                .precioUnitario(detallePedido.getPrecioUnitario())
                .cantidad(detallePedido.getCantidad())
                .pagoSubtotal(detallePedido.getPagoSubtotal())
                .build();
    }

}
