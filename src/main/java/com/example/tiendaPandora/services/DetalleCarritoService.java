package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestDetalleCarrito;
import com.example.tiendaPandora.dtos.response.ResponseDetalleCarrito;
import com.example.tiendaPandora.entities.Carrito;
import com.example.tiendaPandora.entities.DetalleCarrito;
import com.example.tiendaPandora.entities.VarianteProducto;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.mappers.MapperDetalleCarrito;
import com.example.tiendaPandora.repositories.DetalleCarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DetalleCarritoService {

    private final DetalleCarritoRepository detalleCarritoRepository;
    private final CarritoService carritoService;
    private final VarianteProductoService varianteProductoService;

    public DetalleCarrito buscarDetalleCarrito(UUID idDetalleCarrito){
        return detalleCarritoRepository.findById(idDetalleCarrito)
                .orElseThrow(() -> new EntidadNoEncontradaException("Detalle de carrito no encontrado"));
    }

    public List<ResponseDetalleCarrito> listarPorCarrito(UUID idCarrito){
        return detalleCarritoRepository.findByCarrito_IdCarrito(idCarrito)
                .stream()
                .map(MapperDetalleCarrito::toResponse)
                .toList();
    }

    @Transactional
    public void agregarCarrito(UUID idCarrito, UUID idVarianteProducto, RequestDetalleCarrito requestDetalleCarrito){

        VarianteProducto varianteProducto = varianteProductoService.buscarVarianteProducto(idVarianteProducto);

        int filas = varianteProductoService.reducirStock(idVarianteProducto, requestDetalleCarrito.getCantidad());
        if (filas == 0) {
            throw new ReglaDeNegocioException("Stock insuficiente para añadir al carrito");
        }

        Carrito carrito = carritoService.buscarCarritoPorId(idCarrito);

        Optional<DetalleCarrito> detalleCarrito = detalleCarritoRepository.findByCarritoAndVarianteProducto(carrito, varianteProducto);

        if(detalleCarrito.isPresent()) {
            detalleCarrito.get().setCantidad(detalleCarrito.get().getCantidad() + requestDetalleCarrito.getCantidad());
            detalleCarrito.get().calcularSubTotal(varianteProducto.getProducto().getPrecio());
        }else{
            DetalleCarrito detalleCarritoCreado = MapperDetalleCarrito.toEntity(requestDetalleCarrito);

            detalleCarritoCreado.calcularSubTotal(varianteProducto.getProducto().getPrecio());
            detalleCarritoCreado.addVarianteProducto(varianteProducto);
            detalleCarritoCreado.addCarrito(carrito);
        }

        carrito.recalcularPrecioTotal();
    }

    @Transactional
    public void quitarDetalleCarrito(UUID idDetalleCarrito) {

        DetalleCarrito detalleCarrito = buscarDetalleCarrito(idDetalleCarrito);

        Carrito carrito = detalleCarrito.getCarrito();
        List<DetalleCarrito> detalles = carrito.getDetalles();
        detalles.remove(detalleCarrito);

        VarianteProducto varianteProducto = detalleCarrito.getVarianteProducto();
        varianteProducto.restablecerStock(detalleCarrito.getCantidad());
        carrito.recalcularPrecioTotal();

    }



}
