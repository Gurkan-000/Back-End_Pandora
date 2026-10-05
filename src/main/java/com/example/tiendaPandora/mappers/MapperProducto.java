package com.example.tiendaPandora.mappers;

import com.example.tiendaPandora.dtos.request.RequestProducto;
import com.example.tiendaPandora.dtos.response.ResponseProducto;
import com.example.tiendaPandora.entities.Producto;

public class MapperProducto {

    public static Producto toEntity(RequestProducto request){
        return Producto.builder()
                .nombreProducto(request.getNombreProducto())
                .descripcion(request.getDescripcion())
                .build();
    }

    public static ResponseProducto toResponse(Producto producto){
        return ResponseProducto.builder()
                .idProducto(producto.getIdProducto())
                .idSubCategoria(producto.getSubCategoria().getIdSubcategoria())
                .idMarca(producto.getMarca().getIdMarca())
                .nombreProducto(producto.getNombreProducto())
                .descripcion(producto.getDescripcion())
                .nombreMarca(producto.getMarca().getNombreMarca())
                .nombreSubcategoria(producto.getSubCategoria().getNombreSubcategoria())
                .cantidadVariantes(producto.getVariantes().size())
                .build();
    }

}
