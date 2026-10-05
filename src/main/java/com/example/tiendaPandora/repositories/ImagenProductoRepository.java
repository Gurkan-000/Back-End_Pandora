package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, UUID> {
}

