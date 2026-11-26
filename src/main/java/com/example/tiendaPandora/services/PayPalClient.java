package com.example.tiendaPandora.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final ObjectMapper objectMapper;

    public PayPalClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private JsonNode convertirRespuesta(String json) {

        if (json == null || json.isBlank()) {
            throw new IllegalStateException(
                "PayPal devolvió una respuesta vacía"
            );
        }

        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                "No se pudo interpretar la respuesta JSON de PayPal",
                e
            );
        }
    }

    private String obtenerAccessToken() {

        String json = client()
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
                .body(String.class);

        JsonNode response = convertirRespuesta(json);

        if (!response.hasNonNull("access_token")) {
            throw new IllegalStateException(
                "PayPal no devolvió un access_token"
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

        String json = client()
                        .post()
                        .uri("/v2/checkout/orders")
                        .header("Authorization", "Bearer " + token)
                        .header("PayPal-Request-Id", idPedido + "-create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body)
                        .retrieve()
                        .body(String.class);

        return convertirRespuesta(json);
    }

    public JsonNode capturarOrden(
            String idOrdenPayPal,
            String idPedido
    ) {

        String token = obtenerAccessToken();

        String json = client()
                        .post()
                        .uri("/v2/checkout/orders/{id}/capture", idOrdenPayPal)
                        .header("Authorization", "Bearer " + token)
                        .header("PayPal-Request-Id", idPedido + "-capture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(String.class);

        return convertirRespuesta(json);
    }

}