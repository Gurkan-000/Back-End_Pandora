package com.example.tiendaPandora.dtos.request;


import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDetalleCarrito {

    @NotNull(message = "Cantidad no válida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

}

