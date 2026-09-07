package com.example.tiendaPandora.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegister {

    @NotBlank(message="Nombre esta vacio")
    private String nombre;

    @NotBlank(message="Apellido esta vacio")
    private String apellido;

    @NotBlank(message="Usuario esta vacio")
    private String nombreUsuario;

    @Email(message = "Correo invalido")
    @NotBlank(message = "Correo esta vacio")
    private String correo;

    @NotBlank(message="Contraseña esta vacio")
    private String contrasena;

}