package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestCategoria;
import com.example.tiendaPandora.dtos.response.ResponseCategoria;
import com.example.tiendaPandora.entities.Categoria;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperCategoria;
import com.example.tiendaPandora.repositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<ResponseCategoria> listar() {

        return categoriaRepository.findAll()
                .stream()
                .map(MapperCategoria::toResponse)
                .toList();
    }

    public Categoria buscarCategoria(UUID idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Categoria no encontrada")
                );
    }

    public ResponseCategoria buscarPorIdCategoria(UUID idCategoria) {

        Categoria categoria = buscarCategoria(idCategoria);

        return MapperCategoria.toResponse(categoria);
    }

    public ResponseCategoria crear(RequestCategoria request) {

        Categoria categoria = MapperCategoria.toEntity(request);

        categoria = categoriaRepository.save(categoria);

        return MapperCategoria.toResponse(categoria);
    }

    public String actualizar(UUID idCategoria, RequestCategoria request) {

        Categoria categoria = buscarCategoria(idCategoria);

        categoria.setNombreCategoria(request.getNombreCategoria());

        categoriaRepository.save(categoria);

        return "Categoria actualizada correctamente";
    }

    public String eliminar(UUID idCategoria) {

        Categoria categoria = buscarCategoria(idCategoria);

        categoriaRepository.delete(categoria);

        return "Categoria eliminada correctamente";
    }
}
