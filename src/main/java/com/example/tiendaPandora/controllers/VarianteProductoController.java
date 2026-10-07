package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestVarianteProducto;
import com.example.tiendaPandora.dtos.response.ResponseVarianteProducto;
import com.example.tiendaPandora.entities.VarianteProducto;
import com.example.tiendaPandora.services.VarianteProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/variante-producto")
@RequiredArgsConstructor
public class VarianteProductoController {

    private final VarianteProductoService varianteProductoService;

    @PostMapping("/crear/{idProducto}")
    public ResponseEntity<ResponseVarianteProducto> crear(@PathVariable UUID idProducto,
                                                          @Valid@RequestBody RequestVarianteProducto requestVarianteProducto) {

        ResponseVarianteProducto response = varianteProductoService.crear(requestVarianteProducto, idProducto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/actualizar/{idVariante}/producto/{idProducto}")
    public ResponseEntity<String> actualizar(@PathVariable UUID idProducto,
                                                               @PathVariable UUID idVariante,
                                                               @Valid@RequestBody RequestVarianteProducto requestVarianteProducto) {

        String response = varianteProductoService.actualizar(requestVarianteProducto, idProducto, idVariante);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/eliminar/{idVariante}")
    public ResponseEntity<String> actualizar(@PathVariable UUID idVariante) {

        String response = varianteProductoService.eliminar(idVariante);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
