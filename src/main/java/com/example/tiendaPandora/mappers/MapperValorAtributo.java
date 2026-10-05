package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestValorAtributo;
import com.example.tiendaPandora.dtos.response.ResponseValorAtributo;
import com.example.tiendaPandora.entities.ValorAtributo;

public class MapperValorAtributo {

    public static ValorAtributo toEntity(RequestValorAtributo requestValorAtributo){
        return ValorAtributo.builder()
                .valor(requestValorAtributo.getValor())
                .build();
    }

    public static ResponseValorAtributo toResponse(ValorAtributo valorAtributo){
        return ResponseValorAtributo.builder()
                .idValorAtributo(valorAtributo.getIdValorAtributo())
                .idAtributo(valorAtributo.getAtributo().getIdAtributo())
                .valor(valorAtributo.getValor())
                .nombreAtributo(valorAtributo.getAtributo().getNombreAtributo())
                .build();
    }

}
