/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.repository;

import com.example.model.Otp;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author ADMIN
 */

public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findByEmail(String email);
    void deleteByEmail(String email);
}