package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestCategoria;
import com.example.tiendaPandora.dtos.response.ResponseCategoria;
import com.example.tiendaPandora.services.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseCategoria>> listar(){
        List<ResponseCategoria>  response = categoriaService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{idCategoria}")
    public ResponseEntity<ResponseCategoria> buscarPorIdCategoria(@PathVariable UUID idCategoria){
        ResponseCategoria  response = categoriaService.buscarPorIdCategoria(idCategoria);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<ResponseCategoria> crear(@Valid @RequestBody RequestCategoria request){
        ResponseCategoria  response = categoriaService.crear(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/actualizar/{idCategoria}")
    public ResponseEntity<String> actualizar(@PathVariable UUID idCategoria, @Valid @RequestBody RequestCategoria request){
            String response = categoriaService.actualizar(idCategoria, request);
            return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/eliminar/{idCategoria}")
    public ResponseEntity<String> eliminar(@PathVariable UUID idCategoria){
        String response = categoriaService.eliminar(idCategoria);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
