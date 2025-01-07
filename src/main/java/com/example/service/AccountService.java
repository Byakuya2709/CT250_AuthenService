/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.OtpGenerate;
import com.example.model.Account;
import com.example.model.Otp;
import com.example.repository.AccountRepository;
import com.example.request.VerificationRequest;
import java.util.List;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.authen.exception.EmailAlreadyExistsException;
import service.authen.exception.OtpGenerationException;

/**
 *
 * @author admin
 */
@Service
public class AccountService {

    public String encodePassword(String password) {
        // Tạo mã băm (hash) cho mật khẩu với số lần băm là 12
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean verifyPassword(String password, String storedHash) {
        // So sánh mật khẩu người dùng nhập vào với hash lưu trữ
        return BCrypt.checkpw(password, storedHash);
    }
    private static final int OTP_VALIDITY_MINUTES = 10; // Define a constant for OTP validity
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    // Create or Update an account
    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public boolean existedByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    // Find account by email
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    // Find account by ID
    public Optional<Account> findAccountById(String id) {
        return accountRepository.findById(id);
    }

    // Delete account by ID
    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }

    public String generateCode(String email, String type) {
        // Validate input
        if (email.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException("Email hoặc loại OTP không được để trống");
        }

        // Check if the email already exists
        if (existedByEmail(email)) {
            throw new EmailAlreadyExistsException("Email này đã tồn tại!");
        }

        // Clear existing OTPs for this email if any
        List<Otp> existingOtps = otpService.findAllByEmail(email);
        if (!existingOtps.isEmpty()) {
            otpService.deleteAllByEmail(email);
        }

        // Generate OTP
        String otp = OtpGenerate.generateOtp();
        try {
            otpService.saveOtp(email, otp, type, OTP_VALIDITY_MINUTES);
            emailService.sendVerificationEmail(email, otp);
        } catch (Exception e) {
            throw new OtpGenerationException("Có lỗi xảy ra khi lưu mã OTP hoặc gửi email.");
        }

        return "Mã OTP đã được gửi đến email của bạn";
    }

    public String verifyOTP(VerificationRequest verificationReq) {
        String email = verificationReq.getEmail().trim();
        String otp = verificationReq.getCode().trim();
        String type = verificationReq.getType().trim();

        // Input validation
        if (email.isEmpty() || otp.isEmpty() || type.isEmpty()) {
            throw new IllegalArgumentException("Email, mã OTP hoặc loại OTP không hợp lệ.");
        }

        try {
            boolean isOtpValid = otpService.verifyOtp(email, otp, type);
            if (isOtpValid) {
                otpService.deleteOtp(email); // Delete OTP after successful verification
                return "Xác thực OTP thành công";
            } else {
                throw new OtpGenerationException("Mã OTP không hợp lệ hoặc đã hết hạn");
            }
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi không xác định xảy ra khi xác thực mã OTP.");
        }

    }

    public boolean authenticate(String email, String password) {
        // Lấy tài khoản từ cơ sở dữ liệu
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra mật khẩu người dùng nhập vào
        return verifyPassword(password, account.getPassword());
    }
}
