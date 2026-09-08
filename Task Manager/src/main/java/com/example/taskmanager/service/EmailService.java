package com.example.taskmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset your Task Manager password");
        message.setText(
                "We received a request to reset your password.\n\n" +
                        "Click the link below to set a new password. This link expires in 30 minutes:\n" +
                        resetLink + "\n\n" +
                        "If you did not request this, you can safely ignore this email."
        );

        mailSender.send(message);
    }
}