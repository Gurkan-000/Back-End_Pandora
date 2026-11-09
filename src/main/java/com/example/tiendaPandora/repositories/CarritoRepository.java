package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;s

import java.util.UUID;

public interface CarritoRepository extends JpaRepository<Carrito, UUID> {
}
