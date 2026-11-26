package com.example.tiendaPandora.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class PayPalClient {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.base-url}")
    private String baseUrl;

    private RestClient client() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    private String obtenerAccessToken() {

        JsonNode response = client()
                .post()
                .uri("/v1/oauth2/token")
                .headers(headers -> {
                    headers.setBasicAuth(clientId, clientSecret);
                    headers.setContentType(
                            MediaType.APPLICATION_FORM_URLENCODED
                    );
                })
                .body("grant_type=client_credentials")
                .retrieve()
                .body(JsonNode.class);

        if (response == null ||
                !response.hasNonNull("access_token")) {
            throw new IllegalStateException(
                    "No se pudo autenticar con PayPal"
            );
        }

        return response.get("access_token").asText();
    }

    public JsonNode crearOrden(
            String idPedido,
            String monto,
            String moneda
    ) {

        String token = obtenerAccessToken();

        Map<String, Object> amount = Map.of(
                "currency_code", moneda,
                "value", monto
        );

        Map<String, Object> purchaseUnit = Map.of(
                "reference_id", idPedido,
                "amount", amount
        );

        Map<String, Object> body = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(purchaseUnit)
        );

        return client()
                .post()
                .uri("/v2/checkout/orders")
                .header("Authorization", "Bearer " + token)
                .header("PayPal-Request-Id", idPedido + "-create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(JsonNode.class);
    }

    public JsonNode capturarOrden(
            String idOrdenPayPal,
            String idPedido
    ) {

        String token = obtenerAccessToken();

        return client()
                .post()
                .uri("/v2/checkout/orders/{id}/capture",
                        idOrdenPayPal)
                .header("Authorization", "Bearer " + token)
                .header("PayPal-Request-Id", idPedido + "-capture")
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(JsonNode.class);
    }

}