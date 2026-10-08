package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCategoria {

    @NotBlank(message = "Nombre de categoria no valido")
    @Pattern(regexp = "^[A-Za-z]+(?: [A-Za-z]+)*$", message = "El nombre solo puede contener letras sin espacios en los extremos")
    private String nombreCategoria;

}
