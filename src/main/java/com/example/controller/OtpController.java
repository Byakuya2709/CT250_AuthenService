/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.dto.MailRequest;
import com.example.model.Otp;
import com.example.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;
    
    @PostMapping("/generate")
    public ResponseEntity<?> generateCode(@RequestBody MailRequest emailRequest){
        String email = emailRequest.getEmail().trim();
        String otp = emailRequest.getCode().trim();
      
        try {
            otpService.saveOtp(email, otp, 10);
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi lưu mã OTP");
        }
        return ResponseEntity.ok("Mã OTP đã được lưu");
    }
   @PostMapping("/verify")
public ResponseEntity<String> verifyCode(@RequestBody MailRequest verificationReq) {
    String email = verificationReq.getEmail().trim();
    String otp = verificationReq.getCode().trim();

    if (email.isEmpty() || otp.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email hoặc mã OTP không hợp lệ.");
    }

    try {
        boolean isOtpValid = otpService.verifyOtp(email, otp);
        if (isOtpValid) {
            otpService.deleteOtp(email); // Xóa OTP sau khi xác thực thành công
            return ResponseEntity.ok("Xác thực OTP thành công");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mã OTP không hợp lệ hoặc đã hết hạn");
        }
    } catch (Exception e) {
        System.out.println(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi xác thực mã OTP.");
    }
}
    
}


