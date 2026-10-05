package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestSubCategoria {

    @NotBlank(message = "Nombre de sub-categoria no valido")
    @Pattern(regexp = "^[A-Za-z]+$", message = "El nombre solo puede contener letras")
    private String nombreSubCategoria;

}
