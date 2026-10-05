package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.exceptions.TokenException;
import com.example.tiendaPandora.mappers.MapperUsuario;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final EmailService emailService;
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final CookieService serviceCookie;

    public ResponseAuth iniciarSesion(RequestAuth requestAuth, HttpServletResponse response) {
        Usuario usuario = usuarioRepository.findByCorreo(requestAuth.getCorreo())
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado"));

        if(!usuario.getCorreoVerificado()){
            throw new BadCredentialsException("El correo no fue verificado");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        requestAuth.getCorreo(),
                        requestAuth.getContrasena()));

        String accessToken = tokenService.generateAccessToken(usuario);
        String refreshToken = tokenService.generateRefreshToken(usuario);

        serviceCookie.addHttpOnlyCookie("accessToken", accessToken, 30 * 60, response);
        serviceCookie.addHttpOnlyCookie("refreshToken", refreshToken, 7 * 24 * 60 * 60, response);

        return new ResponseAuth(usuario.getRol().toString());
    }

    @Transactional
    public void cerrarSesion(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = serviceCookie.getCookie(request, "accessToken");
        String refreshToken = serviceCookie.getCookie(request, "refreshToken");

        if (accessToken != null) {
            tokenService.revocar(accessToken);
        }

        if (refreshToken != null) {
            tokenService.revocar(refreshToken);
        }

        serviceCookie.deleteCookie("accessToken", response);
        serviceCookie.deleteCookie("refreshToken", response);
    }

    public void registrar(RequestRegister requestRegister) {

        Optional<Usuario> usuarioBuscado = usuarioRepository.findByCorreo(requestRegister.getCorreo());

        if (usuarioBuscado.isPresent()) {
            if(usuarioBuscado.get().getCorreoVerificado()){
                throw new ReglaDeNegocioException("El correo ya está registrado");
            }else{
                throw new ReglaDeNegocioException("Tu cuenta ya fue registrada pero no esta verificada. Revisa la bandeja de tu correo para verificarlo");
            }
        }

        Usuario usuario = MapperUsuario.toEntity(requestRegister, passwordEncoder.encode(requestRegister.getContrasena()));
        usuarioRepository.save(usuario);

        emailService.enviarCorreoVerificacion(
                usuario.getCorreo()
        );
    }

    public void verificarCorreo(String correo) {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                        .orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado"));

        if(usuario.getCorreoVerificado()){
            throw new ReglaDeNegocioException("El correo ya esta verificado");
        }

        usuario.setCorreoVerificado(true);

        usuarioRepository.save(usuario);
    }

    public void refresh(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = serviceCookie.getCookie(request, "refreshToken");

        if (refreshToken == null) {
            throw new TokenException("Refresh token no encontrado");
        }

        Usuario usuario = tokenService.validar(refreshToken);

        String accessToken = tokenService.generateAccessToken(usuario);

        serviceCookie.addHttpOnlyCookie("accessToken", accessToken, 30 * 60, response);
    }
}
