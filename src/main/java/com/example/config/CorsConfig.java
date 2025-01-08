/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author admin
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

   @Override
    public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/**")
                .allowedOrigins("http://localhost:3001","http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // Cho phép tất cả các headers
                .allowCredentials(true);
    }
    }
