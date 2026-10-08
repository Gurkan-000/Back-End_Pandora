package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestConfirmacionContrasena;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseAuth> me(Authentication authentication) {

        ResponseAuth responseAuth = authService.obtenerUsuarioActual(authentication);

        return ResponseEntity.ok(responseAuth);
    }

    @PostMapping("/iniciarSesion")
    public ResponseEntity<ResponseAuth> iniciarSesion(@Valid @RequestBody RequestAuth requestAuth,
                                                      HttpServletResponse response) {

        ResponseAuth responseAuth = authService.iniciarSesion(requestAuth, response);

        return ResponseEntity.ok(responseAuth);
    }

    @PostMapping("/cerrarSesion")
    public ResponseEntity<String> cerrarSesion(HttpServletRequest request,
                                               HttpServletResponse response) {

        authService.cerrarSesion(request, response);

        return ResponseEntity.ok("Se cerro sesion correctamente");
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@Valid @RequestBody RequestRegister requestRegister) {

        authService.registrar(requestRegister);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verificar")
    public ResponseEntity<String> verificarCorreo(@RequestParam String correo) {

        authService.verificarCorreo(correo);

        return ResponseEntity.ok("Correo verificado correctamente");
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        authService.refresh(request, response);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<String> recuperarContrasena(@RequestParam(name = "correo") String correo) {

        String response = authService.enviarCorreoDeRecuperacion(correo);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/confirmar-contrasena")
    public ResponseEntity<String> confirmarContrasena(@Valid @RequestBody RequestConfirmacionContrasena requestConfirmacionContrasena) {

        String response = authService.confirmarContrasena(requestConfirmacionContrasena);

        return ResponseEntity.ok(response);
    }

}
