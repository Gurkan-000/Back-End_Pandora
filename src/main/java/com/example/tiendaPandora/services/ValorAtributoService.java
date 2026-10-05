package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestValorAtributo;
import com.example.tiendaPandora.dtos.response.ResponseValorAtributo;
import com.example.tiendaPandora.entities.Atributo;
import com.example.tiendaPandora.entities.ValorAtributo;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperValorAtributo;
import com.example.tiendaPandora.repositories.ValorAtributoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValorAtributoService {

    private final ValorAtributoRepository valorAtributoRepository;
    private final AtributoService atributoService;

    public List<ResponseValorAtributo> listar() {

        return valorAtributoRepository.findAll()
                .stream()
                .map(MapperValorAtributo::toResponse)
                .toList();
    }

    public ValorAtributo buscarValorAtributo(UUID idValorAtributo) {

        return valorAtributoRepository.findById(idValorAtributo)
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Valor de atributo no encontrado")
                );
    }

    public ResponseValorAtributo buscarPorIdValorAtributo(UUID idValorAtributo) {

        ValorAtributo valorAtributo = buscarValorAtributo(idValorAtributo);

        return MapperValorAtributo.toResponse(valorAtributo);
    }

    public ResponseValorAtributo crear(RequestValorAtributo request, UUID idAtributo) {

        Atributo atributo = atributoService.buscarAtributo(idAtributo);

        ValorAtributo valorAtributo = MapperValorAtributo.toEntity(request);

        atributo.addValorAtributo(valorAtributo);

        valorAtributo = valorAtributoRepository.save(valorAtributo);

        return MapperValorAtributo.toResponse(valorAtributo);
    }

    public String actualizar(UUID idValorAtributo, UUID idAtributo, RequestValorAtributo request) {

        Atributo atributo = atributoService.buscarAtributo(idAtributo);

        ValorAtributo valorAtributo = buscarValorAtributo(idValorAtributo);

        atributo.addValorAtributo(valorAtributo);

        valorAtributo.setValor(request.getValor());

        valorAtributoRepository.save(valorAtributo);

        return "Valor de atributo actualizado correctamente";
    }

    public String eliminar(UUID idValorAtributo) {

        ValorAtributo valorAtributo = buscarValorAtributo(idValorAtributo);

        valorAtributoRepository.delete(valorAtributo);

        return "Valor de atributo eliminado correctamente";
    }

    public Set<UUID> obtenerIdsAtributosDeValores(Set<UUID> idsValores) {
        return valorAtributoRepository.obtenerIdsAtributosDeValores(idsValores);
    }

}
