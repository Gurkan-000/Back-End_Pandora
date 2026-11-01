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
import java.util.regex.Pattern;

@Service
public class ImagenService {

    private static final String PREFIJO_URL = "/api/imagen/";
    private static final Pattern FORMATO_URL_IMAGEN = Pattern.compile(
            "^/api/imagen/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.(jpg|jpeg|png|webp)$"
    );

    private final Path ruta;

    public ImagenService() throws IOException {
        this(Paths.get("imagenes/productos"));
    }

    ImagenService(Path ruta) throws IOException {
        this.ruta = ruta;
        Files.createDirectories(ruta);
    }

    public void validarUrlImagen(String urlImagen) {
        if (urlImagen == null || !FORMATO_URL_IMAGEN.matcher(urlImagen).matches()) {
            throw new IllegalArgumentException("La URL de imagen no tiene un formato válido");
        }

        String nombreArchivo = urlImagen.substring(PREFIJO_URL.length());
        try {
            obtenerPath(nombreArchivo);
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("La imagen indicada no existe", e);
        }
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

        String nombreFinal = UUID.randomUUID() + extraerExtension(imagen.getOriginalFilename());

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

        String extension = extraerExtension(nombreOriginal);

        if (!extension.equals(".jpg")
                && !extension.equals(".jpeg")
                && !extension.equals(".png")
                && !extension.equals(".webp")) {

            throw new IllegalArgumentException(
                    "Tipo de imagen no permitido"
            );
        }

    }

    public String extraerExtension(String nombreImagen){
        return nombreImagen.substring(nombreImagen.lastIndexOf(".")).toLowerCase();
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
