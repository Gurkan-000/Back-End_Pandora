package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestMarca;
import com.example.tiendaPandora.dtos.response.ResponseMarca;
import com.example.tiendaPandora.entities.Marca;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperMarca;
import com.example.tiendaPandora.repositories.MarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public List<ResponseMarca> listar() {

        return marcaRepository.findAll()
                .stream()
                .map(MapperMarca::toResponse)
                .toList();
    }

    public Marca buscarMarca(UUID idMarca){
        return marcaRepository.findById(idMarca)
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Marca no encontrada")
                );
    }

    public ResponseMarca buscarPorIdMarca(UUID idMarca) {

        Marca marca = buscarMarca(idMarca);

        return MapperMarca.toResponse(marca);
    }

    public ResponseMarca crear(RequestMarca request) {

        Marca marca = MapperMarca.toEntity(request);

        marca = marcaRepository.save(marca);

        return MapperMarca.toResponse(marca);
    }

    public String actualizar(UUID idMarca, RequestMarca request) {

        Marca marca = buscarMarca(idMarca);

        marca.setNombreMarca(request.getNombreMarca());

        marcaRepository.save(marca);

        return "Marca actualizada correctamente";
    }

    public String eliminar(UUID idMarca) {

        Marca marca = buscarMarca(idMarca);

        marcaRepository.delete(marca);

        return "Marca eliminada correctamente";
    }
}
