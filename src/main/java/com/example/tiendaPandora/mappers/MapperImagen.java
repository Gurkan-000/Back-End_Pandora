package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.response.ResponseImagen;

public class MapperImagen {

    public static ResponseImagen toResponse(String nombreFinal){
        return ResponseImagen.builder()
                .url("/api/imagen/"+nombreFinal)
                .build();
    }

}
