package com.homedelivery.service;

import com.homedelivery.service.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String email;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender javaMailSender,
                            @Value("${mail.homedelivery}") String email) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.email = email;
    }

    @Override
    public void sendRegistrationEmail(String userEmail, String fullName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(this.email);
            mimeMessageHelper.setReplyTo(this.email);
            mimeMessageHelper.setSubject("Welcome to Home Delivery App!");
            mimeMessageHelper.setText(generateRegistrationEmailBody(fullName), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRegistrationEmailBody(String fullName) {

        Context context = new Context();

        context.setVariable("fullName", fullName);

        return templateEngine.process("registration-email", context);

    }
}
