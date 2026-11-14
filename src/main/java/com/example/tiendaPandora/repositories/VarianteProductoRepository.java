package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Producto;
import com.example.tiendaPandora.entities.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, UUID> {

    Optional<VarianteProducto> findByEsPrincipalAndProducto(Boolean esPrincipal, Producto producto);

    @Modifying
    @Query("""
        UPDATE VarianteProducto v
        SET v.stock = v.stock - :cantidad
        WHERE v.idVarianteProducto = :id AND v.stock >= :cantidad
    """)
    int reducirStock(@Param("id") UUID id, @Param("cantidad") int cantidad);

    @Query("""
        SELECT DISTINCT v
        FROM VarianteProducto v
        JOIN FETCH v.producto p
        LEFT JOIN FETCH v.variantesAtributos va
        LEFT JOIN FETCH va.valor val
        LEFT JOIN FETCH val.atributo a
        WHERE v.stock >= 0
        AND v.stock <= :stockMinimo
        ORDER BY v.stock ASC, p.nombreProducto ASC
    """)
    List<VarianteProducto> obtenerVariantesConStockBajo(
            @Param("stockMinimo") Integer stockMinimo
    );

}

