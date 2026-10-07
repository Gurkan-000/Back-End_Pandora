package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestVarianteProducto;
import com.example.tiendaPandora.dtos.response.ResponseValorAtributo;
import com.example.tiendaPandora.dtos.response.ResponseVarianteProducto;
import com.example.tiendaPandora.entities.VarianteAtributo;
import com.example.tiendaPandora.entities.VarianteProducto;

import java.util.List;

public class MapperVarianteProducto {

    public static VarianteProducto toEntity(RequestVarianteProducto requestVarianteProducto) {
        return VarianteProducto.builder()
                .stock(requestVarianteProducto.getStock())
                .precio(requestVarianteProducto.getPrecio())
                .url(requestVarianteProducto.getUrl())
                .build();
    }

    public static ResponseVarianteProducto toResponse(VarianteProducto varianteProducto) {

        List<ResponseValorAtributo> atributos = varianteProducto.getVariantesAtributos().stream()
                .map(varianteAtributo -> MapperValorAtributo.toResponse(varianteAtributo.getValor()))
                .toList();

        return ResponseVarianteProducto.builder()
                .idVarianteProducto(varianteProducto.getIdVarianteProducto())
                .stock(varianteProducto.getStock())
                .precio(varianteProducto.getPrecio())
                .atributos(atributos)
                .build();
    }

}
