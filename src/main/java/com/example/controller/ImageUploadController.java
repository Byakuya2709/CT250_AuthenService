/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.service.CloudinaryService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB in bytes

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "accountId", required = true) String userId
    ) {
        try {
            if (file == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Image uploaded successfully");
                response.put("imageUrl", "https://res.cloudinary.com/dtza0pk4w/image/upload/v1734758873/default_avatar.jpg");
                return ResponseHandler.resBuilder("Sử dụng avatar mặc định!", HttpStatus.OK, response);
            }

            // Kiểm tra định dạng file
            if (!isValidImage(file)) {
                return ResponseHandler.resBuilder("Invalid image file format", HttpStatus.BAD_REQUEST, null);
            }

            // Kiểm tra kích thước file
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseHandler.resBuilder("File size exceeds the limit", HttpStatus.BAD_REQUEST, null);
            }

            // Gọi service để upload file
            String imageUrl = cloudinaryService.uploadFile(file, "user/" + userId, "avatar");

            // Trả về kết quả
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("imageUrl", imageUrl);

            return ResponseHandler.resBuilder("Image uploaded successfully", HttpStatus.OK, response);

        } catch (IOException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/upload/events/poster")
    public ResponseEntity<?> uploadEventPoster(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "eventTitle", required = true) String eventTitle
    ) {
        try {
            if (file == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Image uploaded successfully");
                response.put("imageUrl", "https://res.cloudinary.com/dtza0pk4w/image/upload/v1736700339/mbs_ortxmh.jpg");
                return ResponseHandler.resBuilder("Sử dụng poster mặc định!", HttpStatus.OK, response);
            }

            // Kiểm tra định dạng file
            if (!isValidImage(file)) {
                return ResponseHandler.resBuilder("Invalid image file format", HttpStatus.BAD_REQUEST, null);
            }

            // Kiểm tra kích thước file
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseHandler.resBuilder("File size exceeds the limit", HttpStatus.BAD_REQUEST, null);
            }
            String sanitizedEventTitle = eventTitle.replaceAll("[^a-zA-Z0-9]", "_");
            // Gọi service để upload file
            String imageUrl = cloudinaryService.uploadFile(file, "events/" + sanitizedEventTitle, "poster");

            // Trả về kết quả
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("imageUrl", imageUrl);

            return ResponseHandler.resBuilder("Image uploaded successfully", HttpStatus.OK, response);

        } catch (IOException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

// Hàm kiểm tra định dạng file (ví dụ: chỉ chấp nhận các file ảnh JPEG, PNG)
    private boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }

    @PostMapping("/upload/events")
    public ResponseEntity<?> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "eventTitle", required = true) String eventTitle) throws IOException {
        try {
            if (files.isEmpty()) {
                return ResponseHandler.resBuilder("Sử dụng poster mặc định!", HttpStatus.OK, "https://res.cloudinary.com/dtza0pk4w/image/upload/v1736700339/mbs_ortxmh.jpg");
            }

            // Làm sạch `eventTitle` và `eventCompany`
            String sanitizedEventTitle = eventTitle.replaceAll("[^a-zA-Z0-9]", "_");

            // Kiểm tra từng file trong danh sách
            for (MultipartFile file : files) {
                if (!isValidImage(file)) {
                    return ResponseHandler.resBuilder("Invalid image file format", HttpStatus.BAD_REQUEST, null);
                }

                if (file.getSize() > MAX_FILE_SIZE) {
                    return ResponseHandler.resBuilder("File size exceeds the limit", HttpStatus.BAD_REQUEST, null);
                }
            }

            // Upload nhiều file
            List<String> imageUrl = cloudinaryService.uploadManyFile(files, "events/" + sanitizedEventTitle);

            return ResponseHandler.resBuilder("Image uploaded successfully", HttpStatus.OK, imageUrl);
        } catch (IOException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

    }

}
