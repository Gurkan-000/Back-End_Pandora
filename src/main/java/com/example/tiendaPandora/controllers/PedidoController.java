package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestPedido;
import com.example.tiendaPandora.dtos.response.ResponseDetallePedido;
import com.example.tiendaPandora.dtos.response.ResponsePedido;
import com.example.tiendaPandora.services.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponsePedido>> listar() {

        List<ResponsePedido> response = pedidoService.listar();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar/cliente")
    public ResponseEntity<List<ResponsePedido>> listarPedidosPorUsuario(@AuthenticationPrincipal UserDetails usuario) {

        List<ResponsePedido> response = pedidoService.listarPorUsuario(usuario.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar/{idPedido}/detalles")
    public ResponseEntity<List<ResponseDetallePedido>> listarDetallePorPedido(@PathVariable UUID idPedido,
                                                                              @AuthenticationPrincipal UserDetails usuario) {

        List<ResponseDetallePedido> response = pedidoService.listarDetallePorPedido(idPedido, usuario);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<ResponsePedido> crear(
            @AuthenticationPrincipal UserDetails usuario,
            @Valid @RequestBody RequestPedido request
    ) {
        ResponsePedido response = pedidoService.crear(usuario.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/enviar/{idPedido}")
    public ResponseEntity<Void> enviar(@PathVariable UUID idPedido) {

        pedidoService.enviar(idPedido);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/entregar/{idPedido}")
    public ResponseEntity<Void> entregar(@PathVariable UUID idPedido) {

        pedidoService.entregar(idPedido);

        return ResponseEntity.noContent().build();
    }

}