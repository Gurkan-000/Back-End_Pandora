package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestPedido;
import com.example.tiendaPandora.dtos.response.ResponseDetallePedido;
import com.example.tiendaPandora.dtos.response.ResponseInfoPedido;
import com.example.tiendaPandora.dtos.response.ResponsePedido;
import com.example.tiendaPandora.entities.DetallePedido;
import com.example.tiendaPandora.entities.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public class MapperPedido {

    public static Pedido toEntity(RequestPedido request) {
        return Pedido.builder()
                .fechaPedido(LocalDateTime.now())
                .informacionAdicional(request.getInformacionAdicional())
                .departamentoEntrega(request.getDepartamentoEntrega())
                .distritoEntrega(request.getDistritoEntrega())
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
                .pagoTotal(pedido.getPagoTotal())
                .build();
    }

    public static ResponseInfoPedido toResponse(Pedido pedido, List<ResponseDetallePedido> detalles) {
        return ResponseInfoPedido.builder()
                .idPedido(pedido.getIdPedido())
                .idUsuario(pedido.getUsuario().getIdUsuario())
                .correo(pedido.getUsuario().getCorreo())
                .informacionAdicional(pedido.getInformacionAdicional())
                .departamentoEntrega(pedido.getDepartamentoEntrega())
                .distritoEntrega(pedido.getDistritoEntrega())
                .direccionEntrega(pedido.getDireccionEntrega())
                .estadoPedido(pedido.getEstadoPedido())
                .fechaPedido(pedido.getFechaPedido())
                .pagoTotal(pedido.getPagoTotal())
                .detalles(detalles)
                .build();
    }

}