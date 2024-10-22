package com.example.controller;

import static com.example.controller.ResponseHandler.resBuilder;
import com.example.dto.MailRequest;
import com.example.dto.VerificationRequest;
import com.example.model.Otp;
import com.example.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        String type = emailRequest.getTypeOtp().trim(); // Thêm trường loại OTP

        if (email.isEmpty() || type.isEmpty()) {
            return resBuilder("Email hoặc loại OTP không được để trống", HttpStatus.BAD_REQUEST, null);
        }

        List<Otp> existingOtps = otpService.findAllByEmail(email);
        if (!existingOtps.isEmpty()) {
            otpService.deleteAllByEmail(email);
        }

        try {
            otpService.saveOtp(email, otp, type, 10); // Ghi lại loại OTP vào dịch vụ
        } catch (Exception e) {
            logger.error("Error saving OTP for email {}: {}", email, e.getMessage());
            return resBuilder("Có lỗi xảy ra khi lưu mã OTP", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseEntity.ok("Mã OTP đã được lưu");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationReq) {
        String email = verificationReq.getEmail().trim();
        String otp = verificationReq.getCode().trim();
        String type = verificationReq.getType().trim(); // Thêm trường loại OTP
        System.out.println(type);

        if (email.isEmpty() || otp.isEmpty() || type.isEmpty()) {
            return resBuilder("Email, mã OTP hoặc loại OTP không hợp lệ.", HttpStatus.BAD_REQUEST, null);
        }

        try {
            boolean isOtpValid = otpService.verifyOtp(email, otp, type); // Gọi phương thức xác minh với loại
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
