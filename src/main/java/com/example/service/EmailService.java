/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author admin
 */
@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerificationEmail(String to, String verificationCode) {
        try {
            // Tạo nội dung email từ template
            Context context = new Context();
            context.setVariable("verificationCode", verificationCode);
            String htmlContent = templateEngine.process("verification-email", context);

            // Tạo email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Account Verification");
            helper.setText(htmlContent, true); // true để gửi email dưới dạng HTML

            // Gửi email
            mailSender.send(message);
            logger.info("Verification email sent successfully to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send verification email to {}", to, e);
            throw new RuntimeException("Failed to send verification email, please try again later.", e);
        }
    }

    public void sendNotificationForBlockedEmail(String to, String link) {
        try {
            // Tạo nội dung email từ template
            Context context = new Context();
            context.setVariable("resetLink", link);
            String htmlContent = templateEngine.process("password-reset-email", context);

            // Tạo email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Account Policy");
            helper.setText(htmlContent, true); // true để gửi email dưới dạng HTML

            // Gửi email
            mailSender.send(message);
            logger.info("Verification email sent successfully to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send verification email to {}", to, e);
            throw new RuntimeException("Failed to send verification email, please try again later.", e);
        }
    }
}
