package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestValorAtributo;
import com.example.tiendaPandora.dtos.response.ResponseValorAtributo;
import com.example.tiendaPandora.services.ValorAtributoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/valor-atributo")
@RequiredArgsConstructor
public class ValorAtributoController {

    private final ValorAtributoService valorAtributoService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseValorAtributo>> listar() {
        List<ResponseValorAtributo> response = valorAtributoService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{idValorAtributo}")
    public ResponseEntity<ResponseValorAtributo> buscarPorIdValorAtributo(
            @PathVariable UUID idValorAtributo) {

        ResponseValorAtributo response =
                valorAtributoService.buscarPorIdValorAtributo(idValorAtributo);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/crear/{idAtributo}")
    public ResponseEntity<ResponseValorAtributo> crear(
            @PathVariable UUID idAtributo,
            @Valid @RequestBody RequestValorAtributo request) {

        ResponseValorAtributo response =
                valorAtributoService.crear(request, idAtributo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/actualizar/{idValorAtributo}/atributo/{idAtributo}")
    public ResponseEntity<String> actualizar(
            @PathVariable UUID idValorAtributo,
            @PathVariable UUID idAtributo,
            @Valid @RequestBody RequestValorAtributo request) {

        String response =
                valorAtributoService.actualizar(
                        idValorAtributo,
                        idAtributo,
                        request
                );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/eliminar/{idValorAtributo}")
    public ResponseEntity<String> eliminar(
            @PathVariable UUID idValorAtributo) {

        String response =
                valorAtributoService.eliminar(idValorAtributo);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

