package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Carrito;
import com.example.tiendaPandora.entities.DetalleCarrito;
import com.example.tiendaPandora.entities.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, UUID> {

    List<DetalleCarrito> findByCarrito_IdCarrito(UUID idCarrito);

    Optional<DetalleCarrito> findByCarritoAndVarianteProducto(Carrito carrito, VarianteProducto varianteProducto);

}
