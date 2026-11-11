package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestDetalleCarrito;
import com.example.tiendaPandora.dtos.response.ResponseCarrito;
import com.example.tiendaPandora.dtos.response.ResponseDetalleCarrito;
import com.example.tiendaPandora.services.CarritoService;
import com.example.tiendaPandora.services.DetalleCarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final DetalleCarritoService detalleCarritoService;

    @GetMapping("/{correo}")
    public ResponseEntity<ResponseCarrito> obtenerCarrito(@PathVariable String correo) {
        ResponseCarrito responseCarrito = carritoService.buscarCarrito(correo);
        return ResponseEntity.ok(responseCarrito);
    }

    @PostMapping("/{correo}/agregar-detalle/{idVarianteProducto}")
    public ResponseEntity<Void> agregarDetalleCarrito(@PathVariable String correo, @PathVariable UUID idVarianteProducto, @Valid @RequestBody RequestDetalleCarrito detalleCarrito){
        detalleCarritoService.agregarCarrito(correo, idVarianteProducto, detalleCarrito);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/quitar-detalle/{idDetalleCarrito}")
    public ResponseEntity<Void> quitarDetalleCarrito(@PathVariable UUID idDetalleCarrito){
        detalleCarritoService.quitarDetalleCarrito(idDetalleCarrito);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
