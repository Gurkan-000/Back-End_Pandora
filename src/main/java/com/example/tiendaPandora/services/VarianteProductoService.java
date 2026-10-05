package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestVarianteProducto;
import com.example.tiendaPandora.dtos.response.ResponseVarianteProducto;
import com.example.tiendaPandora.entities.*;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.mappers.MapperVarianteProducto;
import com.example.tiendaPandora.repositories.VarianteAtributoRepository;
import com.example.tiendaPandora.repositories.VarianteProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VarianteProductoService {

    private final VarianteProductoRepository varianteProductoRepository;
    private final VarianteAtributoRepository varianteAtributoRepository;

    private final ProductoService productoService;
    private final ValorAtributoService valorAtributoService;

    public List<ResponseVarianteProducto> listar(){
        return varianteProductoRepository.findAll().stream()
                .map(MapperVarianteProducto::toResponse)
                .toList();
    }

    public VarianteProducto buscarVarianteProducto(UUID idVarianteProducto){
        return varianteProductoRepository.findById(idVarianteProducto)
                .orElseThrow(() -> new EntidadNoEncontradaException(("Variante producto no encontrado")));
    }

    public ResponseVarianteProducto buscarPorIdVarianteProducto(UUID idVarianteProducto){

        VarianteProducto varianteProducto = buscarVarianteProducto(idVarianteProducto);

        return MapperVarianteProducto.toResponse(varianteProducto);
    }

    @Transactional
    public ResponseVarianteProducto crear(RequestVarianteProducto requestVarianteProducto, UUID idProducto){

        Producto producto = productoService.buscarProducto(idProducto);

        validarVariantesDelProducto(producto, requestVarianteProducto);

        VarianteProducto varianteProducto = MapperVarianteProducto.toEntity(requestVarianteProducto);

        producto.addVariante(varianteProducto);

        for(UUID idValorAtributo : requestVarianteProducto.getIdValorAtributos()){

            ValorAtributo valorAtributo = valorAtributoService.buscarValorAtributo(idValorAtributo);

            VarianteAtributo varianteAtributo = new  VarianteAtributo();

            valorAtributo.addVarianteAtributo(varianteAtributo);
            varianteProducto.addVarianteAtributo(varianteAtributo);
        }

        varianteProducto = varianteProductoRepository.save(varianteProducto);

        return MapperVarianteProducto.toResponse(varianteProducto);
    }

    @Transactional
    public String actualizar(RequestVarianteProducto requestVarianteProducto, UUID idProducto, UUID idVarianteProducto){

        Producto producto = productoService.buscarProducto(idProducto);

        validarVariantesDelProducto(producto, requestVarianteProducto);

        VarianteProducto varianteProducto = buscarVarianteProducto(idVarianteProducto);

        varianteProducto.setPrecio(requestVarianteProducto.getPrecio());
        varianteProducto.setStock(requestVarianteProducto.getStock());

        for(UUID idValorAtributo : requestVarianteProducto.getIdValorAtributos()){
            ValorAtributo valorAtributo = valorAtributoService.buscarValorAtributo(idValorAtributo);

            VarianteAtributo varianteAtributo = new  VarianteAtributo();

            valorAtributo.addVarianteAtributo(varianteAtributo);
            varianteProducto.addVarianteAtributo(varianteAtributo);
        }

        varianteProductoRepository.save(varianteProducto);

        return "Variante de producto actualizado correctamente";
    }

    public String eliminar(UUID idVarianteProducto){

        VarianteProducto varianteProducto = buscarVarianteProducto(idVarianteProducto);

        varianteProductoRepository.delete(varianteProducto);

        return "Variante de producto eliminado correctamente";
    }

    private void validarVariantesDelProducto(Producto producto, RequestVarianteProducto requestVarianteProducto){
        Set<UUID> idsValores = new HashSet<>(requestVarianteProducto.getIdValorAtributos());

        if (idsValores.size() != requestVarianteProducto.getIdValorAtributos().size()) {
            throw new ReglaDeNegocioException("No se pueden repetir valores de atributo");
        }

        Set<UUID> atributosProducto = productoService.obtenerIdsAtributosDelProducto(producto.getIdProducto());

        Set<UUID> atributosEnviados = valorAtributoService.obtenerIdsAtributosDeValores(idsValores);

        if (!atributosProducto.equals(atributosEnviados)) {
            throw new ReglaDeNegocioException(
                    "Los atributos de la variante no coinciden con los atributos del producto"
            );
        }

        if(varianteAtributoRepository.existeCombinacion(producto.getIdProducto(),
                idsValores,
                idsValores.size())){

            throw new ReglaDeNegocioException("Combinacion ya existente");
        }
    }

}
