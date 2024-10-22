package com.example.service;

import com.example.model.Otp;
import com.example.repository.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    @Autowired
    private OtpRepository otpRepository;

    public void saveOtp(String email, String otp, String type, long expiryInMinutes) {
        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setOtpType(type);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(expiryInMinutes));
        otpRepository.save(otpEntity);
        logger.info("OTP saved for email: {}, type: {}, expiryInMinutes: {}", email, type, expiryInMinutes);
    }

    public List<Otp> findAllByEmail(String email) {
        return otpRepository.findAllByEmail(email);
    }

    public void deleteAllByEmail(String email) {
        otpRepository.deleteAllByEmail(email);
        logger.info("Deleted all OTPs for email: {}", email);
    }

    public Otp findLatestOtpByEmailAndType(String email, String type) {
        List<Otp> otps = otpRepository.findByOtpTypeAndEmailOrderByCreatedDateDesc(type, email);
        return otps.isEmpty() ? null : otps.get(0);
    }

    public boolean verifyOtp(String email, String otp, String type) {
        Otp otpEntity = findLatestOtpByEmailAndType(email, type);
        if (otpEntity != null) {
            boolean isValid = otpEntity.getOtp().equals(otp) && LocalDateTime.now().isBefore(otpEntity.getExpiryTime());
            logger.info("OTP verification result for email {} and type {}: {}", email, type, isValid);
            return isValid;
        }
        logger.warn("No OTP found for email: {} and type: {}", email, type);
        return false;
    }

    public void deleteOtp(String email) {
        otpRepository.deleteByEmail(email);
        logger.info("OTP deleted for email: {}", email);
    }
    
    @Scheduled(fixedRate = 1800000) // Thực hiện mỗi 30 phút
    public void deleteExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        List<Otp> expiredOtps = otpRepository.findAllByExpiryTimeBefore(now);
        if (!expiredOtps.isEmpty()) {
            otpRepository.deleteAll(expiredOtps);
            logger.info("Deleted {} expired OTPs", expiredOtps.size());
        } else {
            logger.info("No expired OTPs to delete");
        }
    }
}
