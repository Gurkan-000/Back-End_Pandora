package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarritoRepository extends JpaRepository<Carrito, UUID> {

    Optional<Carrito> findByUsuario_Correo(String correo);

}
