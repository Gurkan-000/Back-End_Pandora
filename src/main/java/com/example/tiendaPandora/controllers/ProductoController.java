package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.request.RequestProducto;
import com.example.tiendaPandora.dtos.response.ResponseAtributo;
import com.example.tiendaPandora.dtos.response.ResponseProducto;
import com.example.tiendaPandora.services.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping("/listar")
    public ResponseEntity<List<ResponseProducto>> listar() {
        List<ResponseProducto> response = productoService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/listar/{idProducto}/atributos")
    public ResponseEntity<Set<ResponseAtributo>> listarAtributosPorIdProduco(@PathVariable UUID idProducto) {
        Set<ResponseAtributo> response = productoService.listarAtributosPorIdProducto(idProducto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<ResponseProducto> buscarPorIdProducto(
            @PathVariable UUID idProducto) {

        ResponseProducto response =
                productoService.buscarPorIdProducto(idProducto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/crear/marca/{idMarca}/subcategoria/{idSubCategoria}")
    public ResponseEntity<ResponseProducto> crear(
            @PathVariable UUID idMarca,
            @PathVariable UUID idSubCategoria,
            @Valid @RequestBody RequestProducto request) {

        ResponseProducto response =
                productoService.crear(request, idMarca, idSubCategoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/actualizar/{idProducto}/marca/{idMarca}/subcategoria/{idSubCategoria}")
    public ResponseEntity<String> actualizar(
            @PathVariable UUID idProducto,
            @PathVariable UUID idMarca,
            @PathVariable UUID idSubCategoria,
            @Valid @RequestBody RequestProducto request) {

        String response = productoService.actualizar(
                            idProducto,
                            idMarca,
                            idSubCategoria,
                            request
                        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/eliminar/{idProducto}")
    public ResponseEntity<String> eliminar(@PathVariable UUID idProducto) {

        String response = productoService.eliminar(idProducto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

