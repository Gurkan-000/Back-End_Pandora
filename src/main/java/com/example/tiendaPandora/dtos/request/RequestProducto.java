package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestProducto {

    @NotBlank(message = "Nombre de producto no valido")
    private String nombreProducto;

    @NotBlank(message = "Descripcion no valido")
    private String descripcion;

    @NotEmpty(message = "No se eligieron los atributos")
    private Set<UUID> idAtributos;

}
