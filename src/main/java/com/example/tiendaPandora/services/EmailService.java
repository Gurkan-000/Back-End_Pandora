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

        String enlace = "http://localhost:8080/api/auth/verificar?correo=" + correo;

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

    public void enviarCorreoDeRecuperacionContrasena(String correo, String token) {

        String enlace = "http://localhost:4200/recuperar-contrasena?token=" + token;

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(correo);
        mensaje.setSubject("Recuperación de contraseña");

        mensaje.setText(
                "Hola,\n\n" +
                        "Hemos recibido una solicitud para recuperar la contraseña de tu cuenta.\n\n" +
                        "Para establecer una nueva contraseña, haz clic en el siguiente enlace:\n\n" +
                        enlace + "\n\n" +
                        "Este enlace es válido por un tiempo limitado.\n\n" +
                        "Si no solicitaste recuperar tu contraseña, puedes ignorar este correo.\n\n" +
                        "Saludos,\n" +
                        "Equipo de soporte"
        );

        mailSender.send(mensaje);
    }

}
