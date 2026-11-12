package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestPedido;
import com.example.tiendaPandora.dtos.response.ResponsePedido;
import com.example.tiendaPandora.entities.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public class MapperPedido {

    public static Pedido toEntity(RequestPedido request) {
        return Pedido.builder()
                .fechaPedido(LocalDateTime.now())
                .direccionEntrega(request.getDireccionEntrega().trim())
                .build();
    }

    public static ResponsePedido toResponse(Pedido pedido) {
        return ResponsePedido.builder()
                .idPedido(pedido.getIdPedido())
                .idUsuario(pedido.getUsuario().getIdUsuario())
                .idVenta(pedido.getVenta().getIdVenta())
                .estadoPedido(pedido.getEstadoPedido())
                .fechaPedido(pedido.getFechaPedido())
                .direccionEntrega(pedido.getDireccionEntrega())
                .pagoTotal(pedido.getPagoTotal())
                .build();
    }

}