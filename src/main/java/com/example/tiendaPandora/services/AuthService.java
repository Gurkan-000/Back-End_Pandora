package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import com.example.tiendaPandora.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TokenBlackListService tokenBlackListService;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, TokenBlackListService tokenBlackListService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenBlackListService = tokenBlackListService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseAuth iniciarSesion(RequestAuth requestAuth) {

        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestAuth.getCorreo(),
                        requestAuth.getContrasena()));

        Usuario usuario =  (Usuario) authentication.getPrincipal();

        String token = jwtService.generateToken(usuario);

        return new ResponseAuth(token);
    }

    @Transactional
    public void registrar(RequestRegister requestRegister) {

    }

}
