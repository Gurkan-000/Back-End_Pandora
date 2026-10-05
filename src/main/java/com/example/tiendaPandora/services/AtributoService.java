package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestAtributo;
import com.example.tiendaPandora.dtos.response.ResponseAtributo;
import com.example.tiendaPandora.entities.Atributo;
import com.example.tiendaPandora.entities.Producto;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperAtributo;
import com.example.tiendaPandora.repositories.AtributoRepository;
import com.example.tiendaPandora.repositories.ProductoAtributoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtributoService {

    private final AtributoRepository atributoRepository;
    private final ProductoAtributoRepository  productoAtributoRepository;

    public List<ResponseAtributo> listar() {
        return atributoRepository.findAll()
                .stream()
                .map(MapperAtributo::toResponse)
                .toList();
    }

    public Atributo buscarAtributo(UUID idAtributo) {
        return atributoRepository.findById(idAtributo)
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Atributo no encontrado")
                );
    }

    public ResponseAtributo buscarPorIdAtributo(UUID idAtributo) {

        Atributo atributo = buscarAtributo(idAtributo);

        return MapperAtributo.toResponse(atributo);
    }


    public ResponseAtributo crear(RequestAtributo request) {

        Atributo atributo = MapperAtributo.toEntity(request);

        atributo = atributoRepository.save(atributo);

        return MapperAtributo.toResponse(atributo);
    }

    public String actualizar(UUID idAtributo, RequestAtributo request) {

        Atributo atributo = buscarAtributo(idAtributo);

        atributo.setNombreAtributo(request.getNombreAtributo());

        atributoRepository.save(atributo);

        return "Atributo actualizado correctamente";
    }

    @Transactional
    public String eliminar(UUID idAtributo) {

        Atributo atributo = buscarAtributo(idAtributo);

        productoAtributoRepository.eliminarPorAtributo(idAtributo);
        atributoRepository.delete(atributo);

        return "Atributo eliminado correctamente";
    }

}
