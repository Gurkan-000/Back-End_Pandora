package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, UUID> {
}

