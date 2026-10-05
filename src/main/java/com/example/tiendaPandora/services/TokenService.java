package com.example.tiendaPandora.services;

import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.entities.enums.TipoToken;
import com.example.tiendaPandora.exceptions.TokenException;
import com.example.tiendaPandora.entities.Token;
import com.example.tiendaPandora.repositories.TokenRepository;
import com.example.tiendaPandora.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public String generateAccessToken(Usuario usuario) {

        Map<String,Object> claims = new HashMap<>();

        claims.put("nombre", usuario.getNombre());
        claims.put("rol", usuario.getRol());

        String token = jwtService.generateToken(claims, usuario, 30, ChronoUnit.MINUTES);
        String jti = UUID.randomUUID().toString();

        Token accessToken = Token.builder()
                .token(token)
                .jti(jti)
                .tipo(TipoToken.ACCESS_TOKEN)
                .expiracion(LocalDateTime.now().plusMinutes(30))
                .revocado(false)
                .usuario(usuario)
                .build();

        tokenRepository.save(accessToken);

        return token;
    }

    public String generateRefreshToken(Usuario usuario) {
        Map<String,Object> claims = new HashMap<>();

        String token = jwtService.generateToken(claims, usuario, 7, ChronoUnit.DAYS);
        String jti = jwtService.extractJti(token);

        Token refreshToken = Token.builder()
                .token(token)
                .jti(jti)
                .tipo(TipoToken.REFRESH_TOKEN)
                .expiracion(LocalDateTime.now().plusDays(7))
                .revocado(false)
                .usuario(usuario)
                .build();

        tokenRepository.save(refreshToken);

        return token;
    }

    public void revocar(String token) {

        String jti = jwtService.extractJti(token);

        Token entityToken = tokenRepository
                .findByJti(jti)
                .orElseThrow(() ->
                        new TokenException("Token no encontrado"));

        entityToken.setRevocado(true);

        tokenRepository.save(entityToken);
    }

    public Usuario validar(String token) {
        String jti = jwtService.extractJti(token);

        Token entityToken = tokenRepository
                .findByJti(jti)
                .orElseThrow(() ->
                        new TokenException("Token no encontrado"));

        if(entityToken.getExpiracion().isBefore(LocalDateTime.now()) || entityToken.getRevocado()) {
            throw new TokenException("Token expirado o revocado");
        }

        return entityToken.getUsuario();
    }

    public boolean esTokenRevocado(String token){
        String jti = jwtService.extractJti(token);
        Token entityToken = tokenRepository
                .findByJti(jti)
                .orElseThrow(() ->
                        new TokenException("Token no encontrado"));

        return entityToken.getRevocado();
    }

}
