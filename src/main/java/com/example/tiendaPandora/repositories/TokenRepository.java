package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    Optional<Token> findByJti(String jti);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Token t
        WHERE t.revocado = true
        OR t.expiracion < :fechaActual
    """)
    public void eliminarTokensRevocados_o_Expirados(
            @Param("fechaActual") LocalDateTime fechaActual
    );

}
