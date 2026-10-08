package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestConfirmacionContrasena {

    @NotBlank(message = "Token invalido")
    private String token;

    @NotBlank(message = "Contrasena nueva invalido")
    private String contrasenaNueva;

    @NotBlank(message = "Contrasena confirmada invalido")
    private String contrasenaConfirmada;

}
