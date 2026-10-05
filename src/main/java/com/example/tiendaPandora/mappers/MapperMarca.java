package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestMarca;
import com.example.tiendaPandora.dtos.response.ResponseMarca;
import com.example.tiendaPandora.entities.Marca;

public class MapperMarca {

    public static Marca toEntity(RequestMarca requestMarca) {
        return Marca.builder()
                .nombreMarca(requestMarca.getNombreMarca())
                .build();

    }

    public static ResponseMarca toResponse(Marca marca) {
        return ResponseMarca.builder()
                .idMarca(marca.getIdMarca())
                .nombreMarca(marca.getNombreMarca())
                .build();

    }

}
