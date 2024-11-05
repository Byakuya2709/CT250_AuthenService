/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dto;

import java.security.SecureRandom;

/**
 *
 * @author admin
 */
public class OtpGenerate {
     private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Sinh số ngẫu nhiên từ 0 đến 9
        }
        return otp.toString();
    }
}
