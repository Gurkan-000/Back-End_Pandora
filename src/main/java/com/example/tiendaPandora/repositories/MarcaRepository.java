package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MarcaRepository extends JpaRepository<Marca, UUID> {
}

