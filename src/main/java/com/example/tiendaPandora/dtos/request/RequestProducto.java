package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestProducto {

    @NotBlank(message = "Nombre de producto no valido")
    @Pattern(regexp = "^[A-Za-z]+(?: [A-Za-z]+)*$", message = "El nombre solo puede contener letras sin espacios en los extremos")
    private String nombreProducto;

    @NotBlank(message = "Descripcion no valido")
    private String descripcion;

    @Positive(message = "Precio debe ser mayor a 0")
    @NotNull(message = "Precio no valido")
    private BigDecimal precio;

    @NotBlank(message = "URL no valido")
    private String urlImagen;

    @NotEmpty(message = "No se eligieron los atributos")
    private Set<UUID> idAtributos;

}
