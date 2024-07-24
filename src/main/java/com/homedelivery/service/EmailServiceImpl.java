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

import java.math.BigDecimal;

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
    public void sendRegistrationEmail(String email, String fullName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(this.email);
            mimeMessageHelper.setReplyTo(this.email);
            mimeMessageHelper.setSubject("Welcome to Home Delivery App!");
            mimeMessageHelper.setText(generateRegistrationEmailBody(fullName), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendMakeOrderEmail(String email, String fullName, Long orderId, BigDecimal totalPrice,
                                   String deliveryAddress, String phoneNumber) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(this.email);
            mimeMessageHelper.setReplyTo(this.email);
            mimeMessageHelper.setSubject("Your order is confirmed");
            mimeMessageHelper.setText(
                    generateMakeOrderEmailBody(fullName, orderId, totalPrice, deliveryAddress, phoneNumber), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateMakeOrderEmailBody(String fullName, Long orderId, BigDecimal totalPrice,
                                              String deliveryAddress, String phoneNumber) {

        Context context = new Context();

        context.setVariable("fullName", fullName);
        context.setVariable("orderId", orderId);
        context.setVariable("totalPrice", totalPrice);
        context.setVariable("deliveryAddress", deliveryAddress);
        context.setVariable("phoneNumber", phoneNumber);

        return templateEngine.process("/email/make-order-email", context);
    }

    private String generateRegistrationEmailBody(String fullName) {

        Context context = new Context();

        context.setVariable("fullName", fullName);

        return templateEngine.process("/email/registration-email", context);
    }
}
