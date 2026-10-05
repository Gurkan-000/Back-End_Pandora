package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public class RequestAtributosDelProducto {

    @NotEmpty(message = "No se eligieron los atributos")
    private Set<UUID> idAtributos;

}
