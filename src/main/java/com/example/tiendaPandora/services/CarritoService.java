
package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.dtos.response.ResponseDetalleCarrito;
import com.example.tiendaPandora.dtos.response.ResponseERPCarrito;
import com.example.tiendaPandora.entities.Carrito;
import com.example.tiendaPandora.entities.DetalleCarrito;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.entities.enums.Rol;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperCarrito;
import com.example.tiendaPandora.mappers.MapperDetalleCarrito;
import com.example.tiendaPandora.mappers.MapperERPCarrito;
import com.example.tiendaPandora.repositories.CarritoRepository;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;

    public Carrito buscarCarritoPorUsuario(String correo) {
        return carritoRepository.findByUsuario_Correo(correo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Carrito no encontrado"));
    }

    public Carrito buscarCarritoPorId(UUID idCarrito){
        return carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new EntidadNoEncontradaException("Carrito no encontrado"));
    }

    public ResponseCarrito buscarCarrito(String correo){
        Carrito carrito = buscarCarritoPorUsuario(correo);
        List<ResponseDetalleCarrito> detalles = carrito.getDetalles().stream()
                .map(MapperDetalleCarrito::toResponse)
                .toList();
        return MapperCarrito.toResponse(carrito, detalles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<ResponseERPCarrito> listar() {
        return carritoRepository.findAll()
                .stream()
                .map(MapperERPCarrito::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional(readOnly = true)
    public ResponseCarrito buscarMiCarrito(UserDetails usuario) {
        return buscarCarrito(usuario.getUsername());
    }

    public void crear(Usuario usuario) {

        Carrito carrito = Carrito.builder().build();

        carrito.addUsuario(usuario);

        carritoRepository.save(carrito);
    }

    public void vaciarCarrito(Carrito carrito){
        carrito.getDetalles().clear();
        carrito.setPrecioTotal(BigDecimal.ZERO);
    }

    @Transactional
    public void vaciarCarrito(UUID idCarrito, UserDetails usuario) {

        Usuario usuarioAutenticado = (Usuario) usuario;

        Carrito carrito = buscarCarritoPorId(idCarrito);

        if (!(usuarioAutenticado.getRol() == Rol.ADMIN || usuarioAutenticado.getUsername().equals(carrito.getUsuario().getUsername()))) {

            throw new AccessDeniedException("No tienes permiso para modificar este carrito");
        }

        vaciarCarritoRestableciendoStock(carrito);
    }

    private void vaciarCarritoRestableciendoStock(Carrito carrito) {

        for (DetalleCarrito detalle : carrito.getDetalles()) {

            detalle.getVarianteProducto()
                    .restablecerStock(detalle.getCantidad());
        }

        carrito.getDetalles().clear();
        carrito.setPrecioTotal(BigDecimal.ZERO);
    }

}

