/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.model.Otp;
import com.example.repository.OtpRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public void saveOtp(String email, String otp, long expiryInMinutes) {
        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(expiryInMinutes));
        otpRepository.save(otpEntity);
    }

    public List<Otp> findAllByEmail(String email) {
        return otpRepository.findAllByEmail(email);
    }

    public void deleteAllByEmail(String email) {
        otpRepository.deleteAllByEmail(email); // No return needed since the repository method returns void
    }

    public Otp findLatestOtpByEmail(String email) {
        List<Otp> otps = otpRepository.findTopByEmailOrderByCreatedDateDesc(email);
        return otps.isEmpty() ? null : otps.get(0); // Return the latest OTP or null if no OTPs exist
    }

    public boolean verifyOtp(String email, String otp) {
        Otp otpEntity = findLatestOtpByEmail(email); // Get the latest OTP directly

        // Check if the OTP entity exists
        if (otpEntity != null) {
            // Validate the OTP and check if it hasn't expired
            return otpEntity.getOtp().equals(otp) && LocalDateTime.now().isBefore(otpEntity.getExpiryTime());
        }
        return false; // Return false if no OTP found or validation fails
    }

    public void deleteOtp(String email) {
        otpRepository.deleteByEmail(email);
    }
    
    @Scheduled(fixedRate = 1800000) // Thực hiện mỗi giờ (3600000 ms)
    public void deleteExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        List<Otp> expiredOtps = otpRepository.findAllByExpiryTimeBefore(now);
        if (!expiredOtps.isEmpty()) {
            otpRepository.deleteAll(expiredOtps);
            System.out.println("Đã xóa tất cả OTP hết hạn");
        }
        else {
            System.out.println("Không có OTP hết hạn để xóa");
        }
    }
}
