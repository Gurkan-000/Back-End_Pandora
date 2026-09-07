package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.entities.enums.Rol;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.mappers.MapperUsuario;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import com.example.tiendaPandora.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    private final ServiceCookie serviceCookie;

    public AuthService(UsuarioRepository usuarioRepository, TokenBlackListService tokenBlackListService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, ServiceCookie serviceCookie) {
        this.usuarioRepository = usuarioRepository;
        this.tokenBlackListService = tokenBlackListService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.serviceCookie = serviceCookie;
    }

    public ResponseAuth iniciarSesion(RequestAuth requestAuth, HttpServletResponse response) {

        Usuario usuario = usuarioRepository.findByCorreo(requestAuth.getCorreo())
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado"));

        if(!usuario.isCorreoVerificado()){
            throw new BadCredentialsException("El correo no fue verificado");
        }

        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestAuth.getCorreo(),
                        requestAuth.getContrasena()));

        String jwt = jwtService.generateToken(usuario);

        serviceCookie.addHttpOnlyCookie("jwt", jwt, 7*24*60*60, response);

        return new ResponseAuth(usuario.getRol().toString());
    }

    @Transactional
    public void registrar(RequestRegister requestRegister) {

        if (usuarioRepository.findByCorreo(requestRegister.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = MapperUsuario.toEntity(requestRegister, passwordEncoder.encode(requestRegister.getContrasena()));

        usuarioRepository.save(usuario);
    }

}
