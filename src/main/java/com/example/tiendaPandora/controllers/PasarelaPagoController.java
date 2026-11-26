package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestCapturaPago;
import com.example.tiendaPandora.dtos.request.RequestPedido;
import com.example.tiendaPandora.dtos.response.ResponseInicioPago;
import com.example.tiendaPandora.dtos.response.ResponsePagoConfirmado;
import com.example.tiendaPandora.services.PasarelaPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pasarela-pago")
@RequiredArgsConstructor
public class PasarelaPagoController {

    private final PasarelaPagoService pasarelaPagoService;

    @PostMapping("/paypal/crear")
    public ResponseEntity<ResponseInicioPago> crearOrden(
            @AuthenticationPrincipal UserDetails usuario,
            @Valid @RequestBody RequestPedido request
    ) {

        ResponseInicioPago response =
                pasarelaPagoService.crearOrden(
                        usuario.getUsername(),
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/paypal/capturar")
    public ResponseEntity<ResponsePagoConfirmado> capturarPago(
            @AuthenticationPrincipal UserDetails usuario,
            @Valid @RequestBody RequestCapturaPago request
    ) {

        ResponsePagoConfirmado response =
                pasarelaPagoService.capturarPago(
                        request.idPedido(),
                        request.idOrdenPayPal(),
                        usuario.getUsername()
                );

        return ResponseEntity.ok(response);
    }

}