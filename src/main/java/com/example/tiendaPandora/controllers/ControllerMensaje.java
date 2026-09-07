package com.example.tiendaPandora.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ControllerMensaje {

    @GetMapping("/mensajeSeguro")
    public String mensajeSeguro(){
        return "Mensaje de seguro";
    }

    @GetMapping("/mensajePublico")
    public String mensajePublico(){
        return "Mensaje de publico";
    }

}
