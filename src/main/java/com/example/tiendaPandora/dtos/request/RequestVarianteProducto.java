package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestVarianteProducto {

    @NotEmpty(message = "No se eligieron los atributos")
    private Set<UUID> idValorAtributos;

    @PositiveOrZero(message = "Stock no valido")
    private Integer stock;

    @Positive(message = "Precio no valido")
    private BigDecimal precio;

}
