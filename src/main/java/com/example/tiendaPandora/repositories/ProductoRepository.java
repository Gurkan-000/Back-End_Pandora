package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.dtos.response.ResponseAtributo;
import com.example.tiendaPandora.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    @Query("""
        SELECT pa.atributo.idAtributo
        FROM ProductoAtributo pa
        WHERE pa.producto.idProducto = :idProducto
    """)
    Set<UUID> obtenerIdsAtributosDelProducto(@Param("idProducto") UUID idProducto);

    @Query("""
        SELECT new com.example.tiendaPandora.dtos.response.ResponseAtributo(pa.atributo.idAtributo, pa.atributo.nombreAtributo)
        FROM ProductoAtributo pa
        WHERE pa.producto.idProducto = :idProducto
    """)
    Set<ResponseAtributo> obtenerAtributosDelProducto(@Param("idProducto") UUID idProducto);

}

