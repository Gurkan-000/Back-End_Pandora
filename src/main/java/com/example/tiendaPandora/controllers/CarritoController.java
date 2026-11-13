package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestDetalleCarrito;
import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.dtos.response.ResponseDetalleCarrito;
import com.example.tiendaPandora.dtos.response.ResponseERPCarrito;
import com.example.tiendaPandora.services.CarritoService;
import com.example.tiendaPandora.services.DetalleCarritoService;
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
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final DetalleCarritoService detalleCarritoService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseERPCarrito>> listarCarritos() {
        return ResponseEntity.ok(carritoService.listar());
    }

    @GetMapping("/{idCarrito}/detalles")
    public ResponseEntity<List<ResponseDetalleCarrito>> listarDetalles(@PathVariable UUID idCarrito) {
        return ResponseEntity.ok(
                detalleCarritoService.listarPorCarrito(idCarrito));
    }

    @GetMapping("/mi-carrito")
    public ResponseEntity<ResponseCarrito> obtenerMiCarrito(@AuthenticationPrincipal UserDetails usuario) {

        return ResponseEntity.ok(carritoService.buscarMiCarrito(usuario));
    }

    @PostMapping("/agregar-detalle/{idVarianteProducto}")
    public ResponseEntity<Void> agregarDetalleCarrito(@AuthenticationPrincipal UserDetails usuario, @PathVariable UUID idVarianteProducto, @Valid @RequestBody RequestDetalleCarrito detalleCarrito){
        detalleCarritoService.agregarCarrito(usuario, idVarianteProducto, detalleCarrito);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/quitar-detalle/{idDetalleCarrito}")
    public ResponseEntity<Void> quitarDetalleCarrito(@PathVariable UUID idDetalleCarrito,
                                                     @AuthenticationPrincipal UserDetails usuario){

        detalleCarritoService.quitarDetalleCarrito(idDetalleCarrito, usuario);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/vaciar/{idCarrito}")
    public ResponseEntity<Void> vaciarCarrito(
            @PathVariable UUID idCarrito,
            @AuthenticationPrincipal UserDetails usuario) {

        carritoService.vaciarCarrito(idCarrito, usuario);

        return ResponseEntity.noContent().build();
    }

}
