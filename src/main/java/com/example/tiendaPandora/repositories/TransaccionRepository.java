package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransaccionRepository extends JpaRepository<Transaccion, UUID> {
}
