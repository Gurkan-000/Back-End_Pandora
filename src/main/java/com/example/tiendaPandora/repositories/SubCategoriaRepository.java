package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, UUID> {
}

