package com.example.tiendaPandora.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoVerificacion(String correo, String token) {

        String enlace = "http://localhost:8081/api/auth/verificar?token=" + token;

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(correo);
        mensaje.setSubject("Verifica tu correo");
        mensaje.setText(
                "Hola,\n\n" +
                        "Gracias por registrarte.\n\n" +
                        "Haz clic en el siguiente enlace para verificar tu correo:\n\n" +
                        enlace + "\n\n" +
                        "Este enlace es válido durante 24 horas."
        );

        mailSender.send(mensaje);
    }

}
