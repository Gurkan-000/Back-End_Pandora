
package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.entities.Carrito;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperCarrito;
import com.example.tiendaPandora.repositories.CarritoRepository;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import com.example.tiendaPandora.security.jwt.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;

    public Carrito buscarCarritoPorUsuario(String correo){
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado"));
        return usuario.getCarrito();
    }

    public Carrito buscarCarritoPorId(UUID idCarrito){
        return carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new EntidadNoEncontradaException("Carrito no encontrado"));
    }

    public ResponseCarrito buscarCarrito(String correo){
        Carrito carrito = buscarCarritoPorUsuario(correo);
        return MapperCarrito.toResponse(carrito);
    }

    public void crear(Usuario usuario) {

        Carrito carrito = Carrito.builder()
                .precioTotal(BigDecimal.ZERO)
                .build();

        carrito.addUsuario(usuario);

        carritoRepository.save(carrito);
    }

}

