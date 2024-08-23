/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.model.Otp;
import com.example.repository.OtpRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

 public boolean verifyOtp(String email, String otp) {
    Optional<Otp> otps = otpRepository.findByEmail(email);
     // Phần này tìm kiếm đối tượng `Otp` trong cơ sở dữ liệu dựa trên email.
    // Phương thức `findByEmail` sẽ trả về một đối tượng `Optional<Otp>`, có thể chứa giá trị `Otp` nếu tìm thấy,
    // hoặc là `Optional.empty()` nếu không tìm thấy.
    if (otps.isPresent()) {
        Otp otpEntity = otps.get();
         // Kiểm tra 2 điều kiện:
  // 1. `otpEntity.getOtp().equals(otp)`: Kiểm tra xem OTP do người dùng cung cấp có khớp với OTP trong cơ sở dữ liệu hay không.
  // 2. `LocalDateTime.now().isBefore(otpEntity.getExpiryTime())`: Kiểm tra xem thời gian hiện tại có trước thời gian hết hạn của OTP hay không.
        if (otpEntity.getOtp().equals(otp) && LocalDateTime.now().isBefore(otpEntity.getExpiryTime())) {
            return true;
        }
    }
    return false;
}

    public void deleteOtp(String email) {
        otpRepository.deleteByEmail(email);
    }
}
