package com.hexa_system.config.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    public void enviarCodigo(String destinatario,String codigo){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(destinatario);
        message.setSubject("ELECTROSYSTEM - CODIGO DE VERIFICACION");
        message.setText("Hola,\n\n" +
                        "Tu código de verificación es: " + codigo + "\n\n" +
                        "Este código expira en 2 minutos.\n\n" +
                        "Si no solicitaste este código, ignora este mensaje.\n\n" +
                        "ElectroSystem"
        );
        mailSender.send(message);
    }
}
