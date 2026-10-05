package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestCategoria;
import com.example.tiendaPandora.dtos.response.ResponseCategoria;
import com.example.tiendaPandora.entities.Categoria;

public class MapperCategoria {

    public static Categoria toEntity(RequestCategoria requestCategoria) {
        return Categoria.builder()
                .nombreCategoria(requestCategoria.getNombreCategoria())
                .build();
    }

    public static ResponseCategoria toResponse(Categoria categoria) {
        return ResponseCategoria.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombreCategoria(categoria.getNombreCategoria())
                .build();
    }

}
