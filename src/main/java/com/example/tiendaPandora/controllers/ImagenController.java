package com.example.tiendaPandora.controllers;

import com.example.tiendaPandora.dtos.response.ResponseImagen;
import com.example.tiendaPandora.services.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/imagen")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenService;

    @PostMapping
    public ResponseEntity<ResponseImagen> subirImagen(@RequestParam("imagen") MultipartFile imagen) throws IOException {

        ResponseImagen imagenResponse = imagenService.subirImagen(imagen);

        return ResponseEntity.status(HttpStatus.CREATED).body(imagenResponse);
    }

    @DeleteMapping("/{nombreArchivo}")
    public ResponseEntity<Void> borrarImagen(@PathVariable String nombreArchivo) throws IOException {

        imagenService.eliminarImagen(nombreArchivo);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreArchivo) throws IOException {

        Path archivo = imagenService.obtenerPath(nombreArchivo);
        String tipoContenido = imagenService.obtenerContentType(archivo);

        Resource recurso = new UrlResource(archivo.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tipoContenido))
                .body(recurso);
    }

}
