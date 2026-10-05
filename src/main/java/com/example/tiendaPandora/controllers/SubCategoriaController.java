package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestSubCategoria;
import com.example.tiendaPandora.dtos.response.ResponseSubCategoria;
import com.example.tiendaPandora.services.SubCategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subcategoria")
@RequiredArgsConstructor
public class SubCategoriaController {

    private final SubCategoriaService subCategoriaService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseSubCategoria>> listar() {
        List<ResponseSubCategoria> response = subCategoriaService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{idSubCategoria}")
    public ResponseEntity<ResponseSubCategoria> buscarPorIdSubCategoria(
            @PathVariable UUID idSubCategoria) {

        ResponseSubCategoria response =
                subCategoriaService.buscarPorIdSubCategoria(idSubCategoria);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/crear/{idCategoria}")
    public ResponseEntity<ResponseSubCategoria> crear(
            @PathVariable UUID idCategoria,
            @Valid @RequestBody RequestSubCategoria request) {

        ResponseSubCategoria response =
                subCategoriaService.crear(request, idCategoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/actualizar/{idSubCategoria}/categoria/{idCategoria}")
    public ResponseEntity<String> actualizar(
            @PathVariable UUID idSubCategoria,
            @PathVariable UUID idCategoria,
            @Valid @RequestBody RequestSubCategoria request) {

        String response =
                subCategoriaService.actualizar(idSubCategoria, idCategoria, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/eliminar/{idSubCategoria}")
    public ResponseEntity<String> eliminar(
            @PathVariable UUID idSubCategoria) {

        String response = subCategoriaService.eliminar(idSubCategoria);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
