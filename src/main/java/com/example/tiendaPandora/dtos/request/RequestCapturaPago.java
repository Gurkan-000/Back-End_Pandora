package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestCapturaPago(

        @NotNull
        UUID idPedido,

        @NotBlank
        String idOrdenPayPal

) {}