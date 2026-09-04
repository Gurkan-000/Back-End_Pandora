package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepositoryUsuario extends JpaRepository<Usuario, UUID> {
}
