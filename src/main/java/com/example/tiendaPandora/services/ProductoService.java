package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestAtributosDelProducto;
import com.example.tiendaPandora.dtos.request.RequestProducto;
import com.example.tiendaPandora.dtos.response.ResponseAtributo;
import com.example.tiendaPandora.dtos.response.ResponseProducto;
import com.example.tiendaPandora.dtos.response.ResponseVarianteProducto;
import com.example.tiendaPandora.entities.*;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.mappers.MapperProducto;
import com.example.tiendaPandora.mappers.MapperVarianteProducto;
import com.example.tiendaPandora.repositories.ProductoAtributoRepository;
import com.example.tiendaPandora.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    private final MarcaService marcaService;
    private final SubCategoriaService subCategoriaService;
    private final AtributoService atributoService;

    public List<ResponseProducto> listar() {
        return productoRepository.findAll()
                .stream()
                .map(MapperProducto::toResponse)
                .toList();
    }

    public Set<ResponseAtributo> listarAtributosPorIdProducto(UUID idProducto) {
        return productoRepository.obtenerAtributosDelProducto(idProducto);
    }

    public List<ResponseVarianteProducto> listarVariantesPorIdProducto(UUID idProducto) {
        Producto producto = buscarProducto(idProducto);
        return producto.getVariantes().stream()
                .map(MapperVarianteProducto::toResponse)
                .toList();
    }

    public Producto buscarProducto(UUID idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Producto no encontrado")
                );
    }

    public ResponseProducto buscarPorIdProducto(UUID idProducto) {

        Producto producto = buscarProducto(idProducto);

        return MapperProducto.toResponse(producto);
    }

    @Transactional
    public ResponseProducto crear(RequestProducto request, UUID idMarca, UUID idSubCategoria) {

        Marca marca = marcaService.buscarMarca(idMarca);
        SubCategoria subCategoria = subCategoriaService.buscarSubCategoria(idSubCategoria);

        Producto producto = MapperProducto.toEntity(request);

        marca.addProducto(producto);
        subCategoria.addProducto(producto);

        for (UUID idAtributo : request.getIdAtributos()) {

            Atributo atributo = atributoService.buscarAtributo(idAtributo);

            ProductoAtributo productoAtributo = new ProductoAtributo();

            atributo.addProductoAtributo(productoAtributo);
            producto.addProductoAtributo(productoAtributo);
        }

        producto = productoRepository.save(producto);

        return MapperProducto.toResponse(producto);
    }

    @Transactional
    public String actualizar(UUID idProducto, UUID idMarca, UUID idSubCategoria, RequestProducto request) {

        Producto producto = buscarProducto(idProducto);

        Set<UUID> atributosSolicitados =
                new HashSet<>(request.getIdAtributos());

        Set<UUID> atributosActuales = producto.getProductosAtributos()
                .stream()
                .map(pa -> pa.getAtributo().getIdAtributo())
                .collect(Collectors.toSet());

        Set<UUID> atributosAgregados = new HashSet<>(atributosSolicitados);
        atributosAgregados.removeAll(atributosActuales);

        Set<UUID> atributosEliminados = new HashSet<>(atributosActuales);
        atributosEliminados.removeAll(atributosSolicitados);

        producto.getProductosAtributos().removeIf(
                pa -> atributosEliminados.contains(
                        pa.getAtributo().getIdAtributo()
                )
        );

        if(!producto.getVariantes().isEmpty() && !atributosAgregados.isEmpty()) {
            throw new ReglaDeNegocioException("No se puede agregar atributos a un producto con variantes");
        }

        Marca marca = marcaService.buscarMarca(idMarca);

        SubCategoria subCategoria =
                subCategoriaService.buscarSubCategoria(idSubCategoria);

        marca.addProducto(producto);
        subCategoria.addProducto(producto);

        producto.setNombreProducto(request.getNombreProducto());
        producto.setDescripcion(request.getDescripcion());

        for (UUID idAtributo : atributosAgregados) {

            Atributo atributo = atributoService.buscarAtributo(idAtributo);

            ProductoAtributo productoAtributo = new ProductoAtributo();

            atributo.addProductoAtributo(productoAtributo);
            producto.addProductoAtributo(productoAtributo);
        }

        productoRepository.save(producto);

        return "Producto actualizado correctamente";
    }

    public String eliminar(UUID idProducto) {

        Producto producto = buscarProducto(idProducto);

        productoRepository.delete(producto);

        return "Producto eliminado correctamente";
    }

    public Set<UUID> obtenerIdsAtributosDelProducto(UUID idProducto){
        return productoRepository.obtenerIdsAtributosDelProducto(idProducto);
    }

}

