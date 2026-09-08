package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
    public ResponseEntity<String> verificarCorreo(@RequestParam String token) {

        authService.verificarCorreo(token);

        return ResponseEntity.ok("Correo verificado correctamente");
    }

}
