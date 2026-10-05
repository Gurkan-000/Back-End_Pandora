package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.ProductoAtributo;
import com.example.tiendaPandora.entities.ProductoAtributoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductoAtributoRepository extends JpaRepository<ProductoAtributo, ProductoAtributoId> {

    @Modifying
    @Query("""
        DELETE FROM ProductoAtributo pa
        WHERE pa.atributo.id = :idAtributo
    """)
    void eliminarPorAtributo(@Param("idAtributo") UUID idAtributo);

}
