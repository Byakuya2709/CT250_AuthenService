package com.example.controller;

import static com.example.controller.ResponseHandler.resBuilder;
import com.example.dto.EmailRequest;
import com.example.dto.OtpGenerate;
import com.example.model.Otp;
import com.example.request.VerificationRequest;
import com.example.service.EmailService;
import com.example.service.OtpService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/otp")
@CrossOrigin(origins = {"http://localhost:3001", "https://byakuya2709.github.io"})
public class Otp2Controller {

    private static final Logger logger = LoggerFactory.getLogger(Otp2Controller.class);
    private static final int OTP_VALIDITY_MINUTES = 10; // Define a constant for OTP validity

    @Autowired
    private OtpService otpService;
    
    @Autowired
    private EmailService emailService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateCode(@RequestBody EmailRequest emailRequest) {
        String email = emailRequest.getEmail().trim();
        String type = emailRequest.getType().trim();
        String otp = OtpGenerate.generateOtp();

        if (email.isEmpty() || type.isEmpty()) {
            return resBuilder("Email hoặc loại OTP không được để trống", HttpStatus.BAD_REQUEST, null);
        }

        List<Otp> existingOtps = otpService.findAllByEmail(email);
        if (!existingOtps.isEmpty()) {
            otpService.deleteAllByEmail(email);
        }

        try {
            otpService.saveOtp(email, otp, type, OTP_VALIDITY_MINUTES);
//            emailService.sendVerificationEmail(email, otp);
        } catch (Exception e) {
            logger.error("Error saving OTP for email {}: {}", email, e.getMessage());
            return resBuilder("Có lỗi xảy ra khi lưu mã OTP", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseHandler.resBuilder("Mã OTP đã được gửi đến email của bạn", HttpStatus.OK, null);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationReq) {
        String email = verificationReq.getEmail().trim();
        String otp = verificationReq.getCode().trim();
        String type = verificationReq.getType().trim();

        if (email.isEmpty() || otp.isEmpty() || type.isEmpty()) {
            return resBuilder("Email, mã OTP hoặc loại OTP không hợp lệ.", HttpStatus.BAD_REQUEST, null);
        }

        try {
            boolean isOtpValid = otpService.verifyOtp(email, otp, type);
            if (isOtpValid) {
                otpService.deleteOtp(email);
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
