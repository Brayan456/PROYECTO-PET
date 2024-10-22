package com.petfrendly.alimentador.servicio;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreo(String para, String asunto, String contenido) {
        MimeMessage mensaje = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(contenido, true);
            mailSender.send(mensaje);
            System.out.println("Correo enviado a: " + para);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
