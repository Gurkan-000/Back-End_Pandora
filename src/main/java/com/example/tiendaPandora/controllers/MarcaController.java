package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestMarca;
import com.example.tiendaPandora.dtos.response.ResponseMarca;
import com.example.tiendaPandora.services.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/marca")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseMarca>> listar() {
        List<ResponseMarca> response = marcaService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{idMarca}")
    public ResponseEntity<ResponseMarca> buscarPorIdMarca(@PathVariable UUID idMarca) {
        ResponseMarca response = marcaService.buscarPorIdMarca(idMarca);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<ResponseMarca> crear(@Valid @RequestBody RequestMarca request) {
        ResponseMarca response = marcaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/actualizar/{idMarca}")
    public ResponseEntity<String> actualizar(
            @PathVariable UUID idMarca,
            @Valid @RequestBody RequestMarca request) {

        String response = marcaService.actualizar(idMarca, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/eliminar/{idMarca}")
    public ResponseEntity<String> eliminar(@PathVariable UUID idMarca) {
        String response = marcaService.eliminar(idMarca);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

