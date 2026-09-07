package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.entities.enums.Rol;

public class MapperUsuario {

    public static Usuario toEntity(RequestRegister requestRegister, String contrasenaEncriptada) {
        return Usuario.builder()
                .nombre(requestRegister.getNombre())
                .apellido(requestRegister.getApellido())
                .correo(requestRegister.getCorreo())
                .contrasena(contrasenaEncriptada)
                .rol(Rol.CLIENTE)
                .correoVerificado(false)
                .build();
    }

}
