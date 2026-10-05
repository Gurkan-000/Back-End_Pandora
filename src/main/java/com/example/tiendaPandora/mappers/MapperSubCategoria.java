package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestSubCategoria;
import com.example.tiendaPandora.dtos.response.ResponseSubCategoria;
import com.example.tiendaPandora.entities.Marca;
import com.example.tiendaPandora.entities.SubCategoria;

public class MapperSubCategoria {

    public static SubCategoria toEntity(RequestSubCategoria requestSubCategoria) {
        return SubCategoria.builder()
                .nombreSubcategoria(requestSubCategoria.getNombreSubCategoria())
                .build();
    }

    public static ResponseSubCategoria toResponse(SubCategoria subCategoria) {
        return ResponseSubCategoria.builder()
                .idSubCategoria(subCategoria.getIdSubcategoria())
                .idCategoria(subCategoria.getCategoria().getIdCategoria())
                .nombreSubCategoria(subCategoria.getNombreSubcategoria())
                .nombreCategoria(subCategoria.getCategoria().getNombreCategoria())
                .build();
    }

}
