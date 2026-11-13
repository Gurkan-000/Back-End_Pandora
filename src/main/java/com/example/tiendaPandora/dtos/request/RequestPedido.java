package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestPedido {

    @NotBlank(message = "La informacion de entrega es obligatoria")
    private String informacionAdicional;

    @NotBlank(message = "La distrito de entrega es obligatoria")
    @Size(max = 40, message = "La dirección no puede superar los 255 caracteres")
    private String distritoEntrega;

    @NotBlank(message = "La departemento de entrega es obligatoria")
    @Size(max = 40, message = "La dirección no puede superar los 255 caracteres")
    private String departamentoEntrega;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    private String direccionEntrega;

}