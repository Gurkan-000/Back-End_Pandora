package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.ValorAtributo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface ValorAtributoRepository extends JpaRepository<ValorAtributo, UUID> {

    @Query("""
        SELECT DISTINCT va.atributo.idAtributo
        FROM ValorAtributo va
        WHERE va.idValorAtributo IN :idsValores
    """)
    Set<UUID> obtenerIdsAtributosDeValores(@Param("idsValores") Set<UUID> idsValores);

}


