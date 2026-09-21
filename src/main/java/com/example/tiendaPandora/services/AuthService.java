package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.entities.EmailTokenVerificacion;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.exceptions.TokenException;
import com.example.tiendaPandora.mappers.MapperEmailToken;
import com.example.tiendaPandora.mappers.MapperUsuario;
import com.example.tiendaPandora.repositories.EmailTokenVerificacionRepository;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import com.example.tiendaPandora.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmailTokenVerificacionRepository emailTokenRepository;

    private final TokenBlackListService tokenBlackListService;
    private final EmailService emailService;
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final CookieService serviceCookie;

    public AuthService(UsuarioRepository usuarioRepository,
                       EmailTokenVerificacionRepository emailTokenRepository,
                       TokenBlackListService tokenBlackListService,
                       EmailService emailService,
                       TokenService tokenService,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       CookieService serviceCookie) {

        this.usuarioRepository = usuarioRepository;
        this.emailTokenRepository = emailTokenRepository;
        this.tokenBlackListService = tokenBlackListService;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.serviceCookie = serviceCookie;
    }

    public ResponseAuth iniciarSesion(RequestAuth requestAuth, HttpServletResponse response) {
        Usuario usuario = usuarioRepository.findByCorreo(requestAuth.getCorreo())
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado"));

        if(!usuario.getCorreoVerificado()){
            throw new BadCredentialsException("El correo no fue verificado");
        }

        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestAuth.getCorreo(),
                        requestAuth.getContrasena()));

        String accessToken = tokenService.generateAccessToken(usuario);
        String refreshToken = tokenService.generateRefreshToken(usuario);

        serviceCookie.addHttpOnlyCookie("accessToken", accessToken, 10, response);
        serviceCookie.addHttpOnlyCookie("refreshToken", refreshToken, 7 * 24 * 60 * 60, response);

        return new ResponseAuth(usuario.getRol().toString());
    }

    @Transactional
    public void cerrarSesion(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = serviceCookie.getCookie(request, "accessToken");
        String refreshToken = serviceCookie.getCookie(request, "refreshToken");

        if (accessToken != null) {
            tokenBlackListService.revocarToken(accessToken);
        }

        if (refreshToken != null) {
            tokenService.revocar(refreshToken);
        }

        tokenBlackListService.revocarToken(accessToken);

        serviceCookie.deleteCookie("accessToken", response);
        serviceCookie.deleteCookie("refreshToken", response);
    }

    @Transactional
    public void registrar(RequestRegister requestRegister) {
        if (usuarioRepository.findByCorreo(requestRegister.getCorreo()).isPresent()) {
            throw new ReglaDeNegocioException("El correo ya está registrado");
        }

        Usuario usuario = MapperUsuario.toEntity(requestRegister, passwordEncoder.encode(requestRegister.getContrasena()));

        usuarioRepository.save(usuario);

        String token = UUID.randomUUID().toString();

        EmailTokenVerificacion tokenVerificacion = MapperEmailToken.toEntity(token, usuario);

        emailTokenRepository.save(tokenVerificacion);

        emailService.enviarCorreoVerificacion(
                usuario.getCorreo(),
                token
        );
    }

    @Transactional
    public void verificarCorreo(String token) {

        EmailTokenVerificacion tokenVerificacion = emailTokenRepository.findByToken(token)
                        .orElseThrow(() -> new TokenException("Token no encontrado"));

        if (tokenVerificacion.getExpiracion().isBefore(LocalDateTime.now())) {

            throw new TokenException("El token ha expirado");
        }

        Usuario usuario = tokenVerificacion.getUsuario();

        usuario.setCorreoVerificado(true);

    }

    public void refresh(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = serviceCookie.getCookie(request, "refreshToken");

        if (refreshToken == null) {
            throw new TokenException("Refresh token no encontrado");
        }

        Usuario usuario = tokenService.validar(refreshToken);

        String accessToken = tokenService.generateAccessToken(usuario);

        serviceCookie.addHttpOnlyCookie("accessToken", accessToken, 10, response);
    }
}
