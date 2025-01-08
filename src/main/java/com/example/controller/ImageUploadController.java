/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.service.CloudinaryService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/media")
public class ImageUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", required = false, defaultValue = "avatar") String fileName
    ) {
        try {
            // Gọi service để upload file
            String imageUrl = cloudinaryService.uploadFile(file, "user/"+fileName, fileName);

            // Trả về kết quả
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("imageUrl", imageUrl);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            // Xử lý lỗi upload
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error uploading image");
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
