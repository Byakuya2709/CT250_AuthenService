/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.model.Account;
import com.example.request.EmailReq;
import com.example.request.VerificationRequest;
import com.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.authen.exception.EmailAlreadyExistsException;
import service.authen.exception.OtpGenerationException;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/auth")
public class AuthenController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateVerificationCode(@RequestBody EmailReq req) {
        try {
            // Validate input
            if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
                return ResponseHandler.resBuilder("Email không được để trống", HttpStatus.BAD_REQUEST, null);
            }

            // Gửi mã OTP đến email
            String message = accountService.generateCode(req.getEmail().trim(), "Registration");

            return ResponseHandler.resBuilder(message, HttpStatus.CREATED, null);
        } catch (EmailAlreadyExistsException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.CONFLICT, null); // 409 Conflict
        } catch (OtpGenerationException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null); // 500 Internal Server Error
        } catch (RuntimeException ex) {
            return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.BAD_REQUEST, null); // 400 Bad Request
        } catch (Exception e) {
            return ResponseHandler.resBuilder("Có lỗi không xác định xảy ra: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody VerificationRequest req) {
    // Validate inputs
    if (req.getCode() == null) {
        return ResponseHandler.resBuilder("Mã không được để trống", HttpStatus.BAD_REQUEST, null);
    }
    // Verify OTP
    try {
        String otpVerificationResult = accountService.verifyOTP(req);
        if (!otpVerificationResult.equals("Xác thực OTP thành công")) {
            return ResponseHandler.resBuilder(otpVerificationResult, HttpStatus.BAD_REQUEST, null); // OTP verification failed
        }

        // If OTP is valid, proceed with account creation
         String encodedPassword = accountService.encodePassword(req.getPassword());
        Account account = new Account(req.getEmail().trim(),encodedPassword , Account.Type.valueOf(req.getRole()));
        Account createdAccount = accountService.saveAccount(account);
        return ResponseHandler.resBuilder("Tạo tài khoản thành công", HttpStatus.CREATED, createdAccount); // Account created successfully

    } catch (RuntimeException ex) {
        return ResponseHandler.resBuilder(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    } catch (Exception ex) {
        return ResponseHandler.resBuilder("Có lỗi xảy ra khi đăng ký tài khoản.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}


}
