package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.entities.EmailTokenVerificacion;
import com.example.tiendaPandora.entities.Usuario;

import java.time.LocalDateTime;

public class MapperEmailToken {

    public static EmailTokenVerificacion toEntity(String token, Usuario usuario){
        return EmailTokenVerificacion.builder()
                .token(token)
                .fechaExpiracion(LocalDateTime.now().plusHours(24))
                .usuario(usuario)
                .build();
    }

}
