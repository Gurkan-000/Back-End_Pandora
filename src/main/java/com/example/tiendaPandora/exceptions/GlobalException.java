package com.example.tiendaPandora.exceptions;

import com.example.tiendaPandora.dtos.response.ResponseError;
import com.example.tiendaPandora.dtos.response.ResponseErrorCampos;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.List;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> exception(Exception e){

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(500)
                .mensaje(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> badCredentialsException(BadCredentialsException e){

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(401)
                .mensaje(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseError> manejarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        String mensaje = "El parámetro '" + ex.getName() + "' tiene un valor inválido: '" + ex.getValue() + "'. Formato esperado: yyyy-MM-dd";
        ResponseError responseError = ResponseError.builder()
                .codigoHttp(400)
                .mensaje(mensaje)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseError> manejarParametroFaltante(MissingServletRequestParameterException ex) {
        String mensaje = "Falta el parámetro obligatorio '" + ex.getParameterName() + "'";

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(400)
                .mensaje(mensaje)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDenied(AccessDeniedException e) {

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(403)
                .mensaje("No tienes permisos para realizar esta operación")
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(responseError);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ResponseError> tokenException(TokenException e){

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(401)
                .mensaje(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseError);
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<ResponseError> reglaDeNegocioException(ReglaDeNegocioException e){

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(409)
                .mensaje(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseError);
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<ResponseError> entidadNoEncontradaException(EntidadNoEncontradaException e){

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(404)
                .mensaje(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseError> UsernameNotFoundException(UsernameNotFoundException e){

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(404)
                .mensaje(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorCampos> methodArgumentNotValidException(MethodArgumentNotValidException e){

        List<String> errores = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        ResponseErrorCampos responseErrorCampos = ResponseErrorCampos.builder()
                .codigoHttp(400)
                .mensajes(errores)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseErrorCampos);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> illegalArgumentException(IllegalArgumentException e){
        ResponseError responseError = ResponseError.builder()
                .codigoHttp(400)
                .mensaje(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<ResponseError> fileAlreadyExistsException(FileAlreadyExistsException e) {

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(409)
                .mensaje("El archivo ya existe.")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseError);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ResponseError> noSuchFileException(NoSuchFileException e) {

        ResponseError responseError = ResponseError.builder()
                .codigoHttp(404)
                .mensaje("La imagen no existe.")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }


}
