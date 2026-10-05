package com.example.tiendaPandora.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoVerificacion(String correo) {

        String enlace = "http://localhost:8081/api/auth/verificar?correo=" + correo;

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(correo);
        mensaje.setSubject("Verifica tu correo");
        mensaje.setText(
                "Hola,\n\n" +
                        "Gracias por registrarte.\n\n" +
                        "Haz clic en el siguiente enlace para verificar tu correo:\n\n" +
                        enlace + "\n\n"
        );

        mailSender.send(mensaje);
    }

}
