package com.example.tiendaPandora.dtos.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseAuth {

    private String rol;

    private String nombre;

    private String apellido;

    private String correo;

}
