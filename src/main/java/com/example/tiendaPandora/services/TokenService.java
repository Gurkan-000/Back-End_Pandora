package com.example.tiendaPandora.services;

import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.exceptions.TokenException;
import com.example.tiendaPandora.repositories.RefreshTokenRepository;
import com.example.tiendaPandora.entities.RefreshToken;
import com.example.tiendaPandora.security.jwt.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public TokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public String generateAccessToken(Usuario usuario) {

        Map<String,Object> claims = new HashMap<>();

        claims.put("nombre", usuario.getNombre());
        claims.put("rol", usuario.getRol());

        return jwtService.generateToken(claims, usuario, 10, ChronoUnit.SECONDS);
    }

    public String generateRefreshToken(Usuario usuario) {
        Map<String,Object> claims = new HashMap<>();

        String token = jwtService.generateToken(claims, usuario, 7, ChronoUnit.DAYS);
        String jti = jwtService.extractJti(token);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .jti(jti)
                .expiracion(LocalDateTime.now().plusDays(7))
                .revocado(false)
                .usuario(usuario)
                .build();

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    public void revocar(String refreshToken) {

        String jti = jwtService.extractJti(refreshToken);

        RefreshToken token = refreshTokenRepository
                .findByJti(jti)
                .orElseThrow(() ->
                        new TokenException("Refresh token no encontrado"));

        token.setRevocado(true);

        refreshTokenRepository.save(token);

    }

    public Usuario validar(String refreshToken) {
        String jti = jwtService.extractJti(refreshToken);

        RefreshToken token = refreshTokenRepository
                .findByJti(jti)
                .orElseThrow(() ->
                        new TokenException("Refresh token no encontrado"));

        if(token.getExpiracion().isBefore(LocalDateTime.now()) || token.getRevocado()) {
            throw new TokenException("Refresh token expirado o revocado");
        }

        return token.getUsuario();
    }
}
