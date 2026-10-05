package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestAtributo;
import com.example.tiendaPandora.dtos.response.ResponseAtributo;
import com.example.tiendaPandora.entities.Atributo;

public class MapperAtributo {

    public static Atributo toEntity(RequestAtributo request) {
        return Atributo.builder()
                .nombreAtributo(request.getNombreAtributo())
                .build();

    }

    public static ResponseAtributo toResponse(Atributo atributo) {
        return ResponseAtributo.builder()
                .idAtributo(atributo.getIdAtributo())
                .nombreAtributo(atributo.getNombreAtributo())
                .build();
    }

}
