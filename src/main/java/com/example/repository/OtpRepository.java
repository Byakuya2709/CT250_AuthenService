/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.repository;

import com.example.model.Otp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */

public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findByEmail(String email);
    void deleteByEmail(String email);
    
    @Transactional
    void deleteAllByEmail(String email);
    
    List<Otp> findAllByEmail(String email);
    
    @Query(value = "{ 'email': ?1, 'otpType': ?0 }", sort = "{ 'createdDate': -1 }")
    List<Otp> findByOtpTypeAndEmailOrderByCreatedDateDesc(String otpType, String email);
    
     List<Otp> findAllByExpiryTimeBefore(LocalDateTime expiryTime);
     List<Otp> findByOtpType(String otpType);
}