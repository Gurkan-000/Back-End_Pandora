package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestAtributo;
import com.example.tiendaPandora.dtos.response.ResponseAtributo;
import com.example.tiendaPandora.services.AtributoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/atributo")
@RequiredArgsConstructor
public class AtributoController {

    private final AtributoService atributoService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseAtributo>> listar() {
        List<ResponseAtributo> response = atributoService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{idAtributo}")
    public ResponseEntity<ResponseAtributo> buscarPorIdAtributo(@PathVariable UUID idAtributo) {
        ResponseAtributo response = atributoService.buscarPorIdAtributo(idAtributo);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<ResponseAtributo> crear(@Valid @RequestBody RequestAtributo request) {
        ResponseAtributo response = atributoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/actualizar/{idAtributo}")
    public ResponseEntity<String> actualizar(
            @PathVariable UUID idAtributo,
            @Valid @RequestBody RequestAtributo request) {

        String response = atributoService.actualizar(idAtributo, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/eliminar/{idAtributo}")
    public ResponseEntity<String> eliminar(@PathVariable UUID idAtributo) {
        String response = atributoService.eliminar(idAtributo);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
