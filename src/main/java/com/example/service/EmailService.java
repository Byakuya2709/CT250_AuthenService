/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.TicketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Base64;


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

    public void sendTicketEmail(String to, TicketResponse ticket) {
        try {
            // Tạo nội dung email từ template
            Context context = new Context();
            context.setVariable("ticket", ticket);
            String htmlContent = templateEngine.process("ticket-email-template", context);

            // Tạo email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Thông tin vé điện tử của bạn");
            helper.setText(htmlContent, true);

            // Kiểm tra xem qrCode có hợp lệ không
            if (ticket.getQrCode() != null && !ticket.getQrCode().isEmpty()) {
                // Xử lý chuỗi Base64 nếu có tiền tố `data:image/png;base64,`
                String base64String = ticket.getQrCode();
                base64String = base64String.replaceFirst("^data:image/\\w+;base64,", "").trim();

                try {
                    byte[] imageBytes = Base64.getDecoder().decode(base64String);
                    helper.addAttachment("ticket_qr.png", new ByteArrayResource(imageBytes));
                } catch (IllegalArgumentException e) {
                    logger.error("Lỗi khi giải mã Base64 của QR Code", e);
                }
            }

            // Gửi email
            mailSender.send(message);
            logger.info("Ticket email sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send ticket email to {}", to, e);
            throw new RuntimeException("Failed to send ticket email, please try again later.", e);
        }
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
