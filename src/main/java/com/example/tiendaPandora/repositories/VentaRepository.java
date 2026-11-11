package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VentaRepository extends JpaRepository<Venta, UUID> {
}
