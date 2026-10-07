package com.example.tiendaPandora.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pasarela-pago")
public class PasarelaPagoController {

    @GetMapping("/comprar")
    public ResponseEntity<String> pasarelaPago() {
        return ResponseEntity.ok("Se compro el producto");
    }

}
