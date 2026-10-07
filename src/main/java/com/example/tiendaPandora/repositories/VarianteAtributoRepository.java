package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.VarianteAtributo;
import com.example.tiendaPandora.entities.VarianteAtributoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface VarianteAtributoRepository extends JpaRepository<VarianteAtributo, UUID> {

    boolean existsById(VarianteAtributoId id);

    @Query("""
        SELECT COUNT(DISTINCT va.atributo.idAtributo)
        FROM ValorAtributo va
        WHERE va.idValorAtributo IN :idsValores
    """)
    long contarAtributosDistintos(@Param("idsValores") Set<UUID> idsValores);

    @Query("""
        SELECT COUNT(vp) > 0
        FROM VarianteProducto vp
        WHERE vp.producto.idProducto = :idProducto
          AND (
              SELECT COUNT(va)
              FROM VarianteAtributo va
              WHERE va.variante = vp
                AND va.valor.idValorAtributo IN :idsValores
          ) = :cantidadValores
    """)
    boolean existeCombinacion(
            @Param("idProducto") UUID idProducto,
            @Param("idsValores") Set<UUID> idsValores,
            @Param("cantidadValores") long cantidadValores
    );


    Set<UUID> id(VarianteAtributoId id);
}


