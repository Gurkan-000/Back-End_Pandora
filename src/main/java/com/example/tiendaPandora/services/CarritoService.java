
package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.entities.Carrito;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperCarrito;
import com.example.tiendaPandora.repositories.CarritoRepository;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;

    public Carrito buscarCarrito(UUID idCarrito) {
        return carritoRepository.findById(idCarrito)
                .orElseThrow(() ->
                        new EntidadNoEncontradaException("Carrito no encontrado")
                );
    }

    public ResponseCarrito buscarPorIdCarrito(UUID idCarrito) {

        Carrito carrito = buscarCarrito(idCarrito);

        return MapperCarrito.toResponse(carrito);
    }

    public void crear(Usuario usuario) {

        Carrito carrito = new Carrito();
        carrito.addUsuario(usuario);

        carritoRepository.save(carrito);
    }

    public String actualizar(UUID idCarrito) {

        Carrito carrito = buscarCarrito(idCarrito);

        carritoRepository.save(carrito);

        return "Carrito actualizado correctamente";
    }

    public String eliminar(UUID idCarrito) {

        Carrito carrito = buscarCarrito(idCarrito);

        carritoRepository.delete(carrito);

        return "Carrito eliminado correctamente";
    }
}

