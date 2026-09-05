package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestAuth;
import com.example.tiendaPandora.dtos.request.RequestRegister;
import com.example.tiendaPandora.dtos.response.ResponseAuth;
import com.example.tiendaPandora.services.AuthService;
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
    public ResponseEntity<ResponseAuth> iniciarSesion(@Valid @RequestBody RequestAuth requestAuth) {

        ResponseAuth responseAuth = authService.iniciarSesion(requestAuth);

        return ResponseEntity.ok(responseAuth);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> iniciarSesion(@Valid @RequestBody RequestRegister requestRegister) {

        authService.registrar(requestRegister);

        return ResponseEntity.noContent().build();
    }

}
