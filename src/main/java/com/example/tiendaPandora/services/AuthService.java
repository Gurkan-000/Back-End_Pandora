package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestConfirmacionContrasena;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.entities.Token;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.entities.enums.Rol;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.exceptions.TokenException;
import com.example.tiendaPandora.mappers.MapperUsuario;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    public Usuario buscarUsuario(String correo){
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe usuario con ese correo"));
    }

    public ResponseAuth iniciarSesion(RequestAuth requestAuth, HttpServletResponse response) {
        Usuario usuario = buscarUsuario(requestAuth.getCorreo());

        if(!usuario.getCorreoVerificado()){
            throw new BadCredentialsException("El correo no fue verificado");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        requestAuth.getCorreo(),
                        requestAuth.getContrasena()));

        String accessToken = tokenService.generateAccessToken(usuario);
        String refreshToken = tokenService.generateRefreshToken(usuario);

        serviceCookie.addHttpOnlyCookie("accessToken", accessToken, 30, response);
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

    @Transactional
    public void registrar(RequestRegister requestRegister) {

        Optional<Usuario> usuarioBuscado = usuarioRepository.findByCorreo(requestRegister.getCorreo());

        if(usuarioBuscado.isPresent()){
            if(usuarioBuscado.get().getCorreoVerificado()){
                throw new ReglaDeNegocioException("El correo ya está registrado");
            }
        }else {
            Usuario usuario = MapperUsuario.toEntity(requestRegister, passwordEncoder.encode(requestRegister.getContrasena()));
            usuarioRepository.save(usuario);
        }

        emailService.enviarCorreoVerificacion(
                requestRegister.getCorreo()
        );

    }

    public void verificarCorreo(String correo) {

        Usuario usuario = buscarUsuario(correo);

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

        Token token = tokenService.buscarToken(refreshToken);
        tokenService.validar(token);
        Usuario usuario = token.getUsuario();

        String accessToken = tokenService.generateAccessToken(usuario);

        serviceCookie.addHttpOnlyCookie("accessToken", accessToken, 30 * 60, response);
    }

    public String enviarCorreoDeRecuperacion(String correo) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if (usuarioOptional.isPresent()) {

            Usuario usuario = usuarioOptional.get();

            if (usuario.getRol() != Rol.ADMIN) {
                String token =
                        tokenService.generateTokenDeRecuperacionContrasena(usuario);

                emailService.enviarCorreoDeRecuperacionContrasena(
                        usuario.getCorreo(),
                        token
                );
            }
        }

        return "Si el correo está registrado, recibirás un enlace para recuperar tu contraseña.";
    }

    @Transactional
    public String confirmarContrasena(
            @Valid RequestConfirmacionContrasena request) {

        Token token = tokenService.validarTokenRecuperacion(request.getToken());

        Usuario usuario = token.getUsuario();

        String contrasenaNueva = request.getContrasenaNueva();
        String contrasenaConfirmada = request.getContrasenaConfirmada();

        if (!contrasenaNueva.equals(contrasenaConfirmada)) {
            throw new ReglaDeNegocioException(
                    "Las contraseñas no coinciden"
            );
        }

        usuario.setContrasena(passwordEncoder.encode(contrasenaNueva));
        token.setRevocado(true);

        return "Contraseña actualizada correctamente";
    }

    public ResponseAuth obtenerUsuarioActual(Authentication authentication) {
        String correo = authentication.getName();

        Usuario usuario = buscarUsuario(correo);

        return MapperUsuario.toResponse(usuario);
    }
}
