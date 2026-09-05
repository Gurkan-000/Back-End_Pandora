package com.example.tiendaPandora.security.oauth;

import com.example.tiendaPandora.entities.enums.Rol;
import com.example.tiendaPandora.entities.Usuario;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import com.example.tiendaPandora.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler  implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String googleId = oidcUser.getSubject();
        String correo = oidcUser.getEmail();
        String nombre = oidcUser.getGivenName();
        String apellido = oidcUser.getFamilyName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseGet(() -> {
                    Usuario nuevoUsuario = Usuario.builder()
                            .nombre(nombre)
                            .apellido(apellido)
                            .correo(correo)
                            .googleId(googleId)
                            .rol(Rol.CLIENTE)
                            .contrasena(null)
                            .build();

                    return usuarioRepository.save(nuevoUsuario);
                });

        if (usuario.getGoogleId() == null) {
            usuario.setGoogleId(googleId);
            usuarioRepository.save(usuario);
        }

        String token = jwtService.generateToken(usuario);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"token\":\"" + token + "\"}"
        );

    }
}
