package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestPedido;
import com.example.tiendaPandora.dtos.response.ResponseInicioPago;
import com.example.tiendaPandora.dtos.response.ResponsePagoConfirmado;
import com.example.tiendaPandora.entities.Pedido;
import com.example.tiendaPandora.entities.Transaccion;
import com.example.tiendaPandora.entities.enums.EstadoTransaccion;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasarelaPagoService {

    private final PedidoService pedidoService;
    private final PayPalClient payPalClient;

    @Value("${paypal.currency}")
    private String moneda;

    public ResponseInicioPago crearOrden(
            String correo,
            RequestPedido request
    ) {

        Pedido pedido = pedidoService.crearParaPago(
                correo, request
        );

        try {

            String monto = pedido.getPagoTotal()
                    .setScale(2, RoundingMode.HALF_UP)
                    .toPlainString();

            JsonNode response = payPalClient.crearOrden(
                    pedido.getIdPedido().toString(),
                    monto,
                    moneda
            );

            String idOrden = response.path("id").asText();

            if (idOrden.isBlank()) {
                throw new IllegalStateException(
                        "PayPal no devolvió el ID de la orden"
                );
            }

            pedidoService.registrarOrdenPayPal(
                    pedido.getIdPedido(),
                    correo,
                    idOrden
            );

            return new ResponseInicioPago(
                    pedido.getIdPedido(),
                    idOrden,
                    monto,
                    moneda
            );

        } catch (RuntimeException ex) {

            pedidoService.marcarPagoFallido(
                    pedido.getIdPedido(), correo
            );

            throw ex;
        }
    }

    public ResponsePagoConfirmado capturarPago(
            UUID idPedido,
            String idOrdenPayPal,
            String correo
    ) {

        Transaccion transaccion =
                pedidoService.obtenerTransaccionParaPago(
                        idPedido, correo
                );

        if (transaccion.getEstadoTransaccion()
                == EstadoTransaccion.PROCEDE) {

            return new ResponsePagoConfirmado(
                    idPedido,
                    "PROCEDE",
                    transaccion.getMonto()
            );
        }

        if (!idOrdenPayPal.equals(
                transaccion.getIdOrdenPayPal())) {

            throw new IllegalArgumentException(
                    "La orden PayPal no corresponde al pedido"
            );
        }

        JsonNode response = payPalClient.capturarOrden(
                idOrdenPayPal,
                idPedido.toString()
        );

        JsonNode capture = response
                .path("purchase_units")
                .path(0)
                .path("payments")
                .path("captures")
                .path(0);

        String estado = capture.path("status").asText();

        String idCaptura = capture.path("id").asText();

        String montoCapturado = capture
                .path("amount")
                .path("value")
                .asText();

        String monedaCapturada = capture
                .path("amount")
                .path("currency_code")
                .asText();

        BigDecimal monto = new BigDecimal(montoCapturado);

        if (!"COMPLETED".equals(estado)
                || idCaptura.isBlank()
                || !moneda.equals(monedaCapturada)
                || monto.compareTo(transaccion.getMonto()) != 0) {

            throw new IllegalStateException(
                    "PayPal no confirmó un pago válido"
            );
        }

        pedidoService.confirmarPago(
                idPedido,
                correo,
                idCaptura
        );

        return new ResponsePagoConfirmado(
                idPedido,
                "PROCEDE",
                monto
        );
    }

}