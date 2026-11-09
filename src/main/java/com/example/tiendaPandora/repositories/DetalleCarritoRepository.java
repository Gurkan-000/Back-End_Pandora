package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.DetalleCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, UUID> {

    List<DetalleCarrito> findByCarrito_IdCarrito(UUID idCarrito);

    @Modifying
    @Query("""
    UPDATE VarianteProducto v
    SET v.stock = v.stock - :cantidad
    WHERE v.idVarianteProducto = :id AND v.stock >= :cantidad
    """)
    int reducirStock(@Param("id") UUID id, @Param("cantidad") int cantidad);

}
