package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestSubCategoria;
import com.example.tiendaPandora.dtos.response.ResponseSubCategoria;
import com.example.tiendaPandora.entities.Categoria;
import com.example.tiendaPandora.entities.SubCategoria;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperSubCategoria;
import com.example.tiendaPandora.repositories.SubCategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubCategoriaService {

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaService categoriaService;

    public List<ResponseSubCategoria> listar() {

        return subCategoriaRepository.findAll()
                .stream()
                .map(MapperSubCategoria::toResponse)
                .toList();
    }

    public SubCategoria buscarSubCategoria(UUID idSubCategoria) {
        return subCategoriaRepository.findById(idSubCategoria)
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Subcategoria no encontrada")
                );
    }

    public ResponseSubCategoria buscarPorIdSubCategoria(UUID idSubCategoria) {

        SubCategoria subCategoria = buscarSubCategoria(idSubCategoria);

        return MapperSubCategoria.toResponse(subCategoria);
    }

    public ResponseSubCategoria crear(RequestSubCategoria request, UUID idCategoria) {

        Categoria categoria = categoriaService.buscarCategoria(idCategoria);

        SubCategoria subCategoria = MapperSubCategoria.toEntity(request);

        categoria.addSubCategoria(subCategoria);

        subCategoria = subCategoriaRepository.save(subCategoria);

        return MapperSubCategoria.toResponse(subCategoria);
    }

    public String actualizar(UUID idSubCategoria, UUID idCategoria, RequestSubCategoria request) {

        Categoria categoria = categoriaService.buscarCategoria(idCategoria);

        SubCategoria subCategoria = buscarSubCategoria(idSubCategoria);

        categoria.addSubCategoria(subCategoria);
        subCategoria.setNombreSubcategoria(request.getNombreSubCategoria());

        subCategoriaRepository.save(subCategoria);

        return "Subcategoria actualizada correctamente";
    }

    public String eliminar(UUID idSubCategoria) {

        SubCategoria subCategoria = buscarSubCategoria(idSubCategoria);

        subCategoriaRepository.delete(subCategoria);

        return "Subcategoria eliminada correctamente";
    }
}
