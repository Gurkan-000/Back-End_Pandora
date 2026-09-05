package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, UUID> {

    boolean existsByToken(String token);

}