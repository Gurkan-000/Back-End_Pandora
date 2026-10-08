package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.response.ResponseImagen;
import com.example.tiendaPandora.mappers.MapperImagen;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImagenService {

    private final Path ruta = Paths.get("imagenes/productos");

    public ImagenService() throws IOException {
        Files.createDirectories(ruta);
    }

    public ResponseImagen subirImagen(MultipartFile imagen) throws IOException {

        if(imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("Se envio una imagen vacia");
        }

        InputStream inputStream = imagen.getInputStream();
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        if(bufferedImage == null) {
            throw new IllegalArgumentException("El archivo no es una imagen válida");
        }

        validarExtension(imagen);

        String nombreFinal = UUID.randomUUID() + ".webp";

        Path destino = ruta.resolve(nombreFinal);

        Files.copy(imagen.getInputStream(), destino);

        return MapperImagen.toResponse(nombreFinal);
    }

    public void eliminarImagen(String imagen) throws IOException{
        Path archivo = obtenerPath(imagen);
        Files.delete(archivo);
    }

    private void validarExtension(MultipartFile imagen) {

        String nombreOriginal = imagen.getOriginalFilename();

        if (nombreOriginal == null || !nombreOriginal.contains(".")) {
            throw new IllegalArgumentException(
                    "La imagen no tiene una extensión válida"
            );
        }

        String extension = nombreOriginal
                .substring(nombreOriginal.lastIndexOf("."))
                .toLowerCase();

        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")) {

            throw new IllegalArgumentException(
                    "Tipo de imagen no permitido"
            );
        }

    }

    public Path obtenerPath(String nombreArchivo) throws NoSuchFileException {

        Path archivo = ruta
                .resolve(nombreArchivo)
                .normalize();

        if (!archivo.startsWith(ruta)) {
            throw new IllegalArgumentException("Ruta de archivo no válida");
        }

        if (!Files.exists(archivo) || !Files.isRegularFile(archivo)) {
            throw new NoSuchFileException(nombreArchivo);
        }

        return archivo;
    }

    public String obtenerContentType(Path archivo) throws IOException {
        return Files.probeContentType(archivo);
    }

}
