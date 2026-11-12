package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestDetalleCarrito;
import com.example.tiendaPandora.dtos.response.ResponseDetalleCarrito;
import com.example.tiendaPandora.entities.Carrito;
import com.example.tiendaPandora.entities.DetalleCarrito;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.entities.VarianteProducto;
import com.example.tiendaPandora.entities.enums.Rol;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.mappers.MapperDetalleCarrito;
import com.example.tiendaPandora.repositories.DetalleCarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<ResponseDetalleCarrito> listarPorCarrito(UUID idCarrito) {

        Carrito carrito = carritoService.buscarCarritoPorId(idCarrito);

        return carrito.getDetalles()
                .stream()
                .map(MapperDetalleCarrito::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public void agregarCarrito(UserDetails usuario, UUID idVarianteProducto, RequestDetalleCarrito requestDetalleCarrito){

        Carrito carrito = carritoService.buscarCarritoPorUsuario(usuario.getUsername());

        VarianteProducto varianteProducto = varianteProductoService.buscarVarianteProducto(idVarianteProducto);

        int filas = varianteProductoService.reducirStock(idVarianteProducto, requestDetalleCarrito.getCantidad());
        if (filas == 0) {
            throw new ReglaDeNegocioException("Stock insuficiente para añadir al carrito");
        }

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
    public void quitarDetalleCarrito(UUID idDetalleCarrito, UserDetails usuario) {

        DetalleCarrito detalleCarrito = buscarDetalleCarrito(idDetalleCarrito);
        Carrito carrito = detalleCarrito.getCarrito();

        Usuario usuarioAutenticado = (Usuario) usuario;

        if (!(usuarioAutenticado.getRol() == Rol.ADMIN || usuarioAutenticado.getUsername().equals(carrito.getUsuario().getUsername()))) {
            throw new AccessDeniedException("No tienes permiso para modificar este carrito");
        }

        List<DetalleCarrito> detalles = carrito.getDetalles();
        detalles.remove(detalleCarrito);

        VarianteProducto varianteProducto = detalleCarrito.getVarianteProducto();
        varianteProducto.restablecerStock(detalleCarrito.getCantidad());
        carrito.recalcularPrecioTotal();

    }

}
