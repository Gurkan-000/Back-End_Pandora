package com.example.tiendaPandora.services;

import java.util.Date;

import com.example.tiendaPandora.repositories.TokenBlackListRepository;
import org.springframework.stereotype.Service;

import com.example.tiendaPandora.entities.TokenBlackList;
import com.example.tiendaPandora.security.jwt.JwtService;

@Service
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;
    private final JwtService jwtService;

    public TokenBlackListService(TokenBlackListRepository tokenBlackListRepository, JwtService jwtService) {
        this.tokenBlackListRepository = tokenBlackListRepository;
        this.jwtService = jwtService;
    }

    public void revocarToken(String token) {

        Date fechaExpiracion = jwtService.extractExpiration(token);

        TokenBlackList tokenBlacklist = TokenBlackList.builder()
                .token(token)
                .fechaExpiracion(fechaExpiracion)
                .build();

        tokenBlackListRepository.save(tokenBlacklist);
    }

    public boolean esTokenInvalido(String token) {
        return tokenBlackListRepository.existsByToken(token);
    }

}
