package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    Optional<Token> findByJti(String jti);

}
