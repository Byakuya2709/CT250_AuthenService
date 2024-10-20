/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import static com.example.controller.ResponseHandler.resBuilder;
import com.example.dto.MailRequest;
import com.example.model.Otp;
import com.example.service.OtpService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Other imports...

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateCode(@RequestBody MailRequest emailRequest) {
        String email = emailRequest.getEmail().trim();
        String otp = emailRequest.getCode().trim();

        if (email.isEmpty()) {
            return resBuilder("Email không được để trống", HttpStatus.BAD_REQUEST, null);
        }

        List<Otp> existingOtps = otpService.findAllByEmail(email);
        if (!existingOtps.isEmpty()) {
            otpService.deleteAllByEmail(email);
        }

        try {
            otpService.saveOtp(email, otp, 10);
        } catch (Exception e) {
            logger.error("Error saving OTP for email {}: {}", email, e.getMessage());
            return resBuilder("Có lỗi xảy ra khi lưu mã OTP", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseEntity.ok("Mã OTP đã được lưu");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody MailRequest verificationReq) {
        String email = verificationReq.getEmail().trim();
        String otp = verificationReq.getCode().trim();

        if (email.isEmpty() || otp.isEmpty()) {
            return resBuilder("Email hoặc mã OTP không hợp lệ.", HttpStatus.BAD_REQUEST, null);
        }

        try {
            boolean isOtpValid = otpService.verifyOtp(email, otp);
            if (isOtpValid) {
                otpService.deleteOtp(email); // Xóa OTP sau khi xác thực thành công
                return resBuilder("Xác thực OTP thành công", HttpStatus.OK, null);
            } else {
                return resBuilder("Mã OTP không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST, null);
            }
        } catch (Exception e) {
            logger.error("Error verifying OTP for email {}: {}", email, e.getMessage());
            return resBuilder("Có lỗi xảy ra khi xác thực mã OTP.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
