package com.example.tiendaPandora.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImagenServiceTest {

    @TempDir
    Path directorioTemporal;

    @Test
    void rechazaUrlImagenQueNoTieneElFormatoEsperado() throws IOException {
        ImagenService imagenService = new ImagenService(directorioTemporal);

        assertThrows(IllegalArgumentException.class,
                () -> imagenService.validarUrlImagen("url"));
    }

    @Test
    void aceptaUrlDeImagenSubidaQueExiste() throws IOException {
        ImagenService imagenService = new ImagenService(directorioTemporal);
        String nombreArchivo = UUID.randomUUID() + ".png";
        Files.createFile(directorioTemporal.resolve(nombreArchivo));

        assertDoesNotThrow(() -> imagenService.validarUrlImagen("/api/imagen/" + nombreArchivo));
    }

    @Test
    void rechazaUrlDeImagenSiElArchivoNoExiste() throws IOException {
        ImagenService imagenService = new ImagenService(directorioTemporal);
        String nombreArchivo = UUID.randomUUID() + ".png";

        assertThrows(IllegalArgumentException.class,
                () -> imagenService.validarUrlImagen("/api/imagen/" + nombreArchivo));
    }
}