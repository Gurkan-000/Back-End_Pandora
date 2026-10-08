package com.example.tiendaPandora.services;

import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.entities.enums.TipoToken;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
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

    public String generateTokenDeRecuperacionContrasena(Usuario usuario){
        Map<String,Object> claims = new HashMap<>();

        claims.put("correo", usuario.getCorreo());

        String jti = UUID.randomUUID().toString();
        String token = jwtService.generateToken(claims, usuario, jti, 30, ChronoUnit.MINUTES);

        Token accessToken = Token.builder()
                .token(token)
                .jti(jti)
                .tipo(TipoToken.RECUPERACION_CONTRASENA)
                .expiracion(LocalDateTime.now().plusMinutes(30))
                .revocado(false)
                .usuario(usuario)
                .build();

        tokenRepository.save(accessToken);

        return token;
    }

    public String generateAccessToken(Usuario usuario) {

        Map<String,Object> claims = new HashMap<>();

        claims.put("nombre", usuario.getNombre());
        claims.put("rol", usuario.getRol());

        String jti = UUID.randomUUID().toString();
        String token = jwtService.generateToken(claims, usuario, jti, 30, ChronoUnit.MINUTES);

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

        String jti = UUID.randomUUID().toString();
        String token = jwtService.generateToken(claims, usuario, jti, 7, ChronoUnit.DAYS);

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

    public Token buscarToken(String token){
        String jti = jwtService.extractJti(token);
        return tokenRepository
                .findByJti(jti)
                .orElseThrow(() ->
                        new TokenException("Token no encontrado"));
    }

    public void revocar(String token) {
        Token tokenEntity = buscarToken(token);
        tokenEntity.setRevocado(true);
        tokenRepository.save(tokenEntity);
    }

    public void validar(Token token) {
        if(!jwtService.isTokenValid(token.getToken(), token.getUsuario()) || token.getRevocado()) {
            throw new TokenException("Token expirado o revocado");
        }
    }

    public boolean esTokenRevocado(String token) {
        Token entityToken = buscarToken(token);
        return entityToken.getRevocado();
    }

    public Token validarTokenRecuperacion(String token) {
        Token tokenEntity = buscarToken(token);

        if(tokenEntity.getTipo() != TipoToken.RECUPERACION_CONTRASENA) {
            throw new ReglaDeNegocioException("Token no valido");
        }
        validar(tokenEntity);

        return tokenEntity;
    }
}
