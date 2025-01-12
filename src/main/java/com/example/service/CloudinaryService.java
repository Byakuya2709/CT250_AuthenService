/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.cloudinary.Cloudinary;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folder, String fileName) throws IOException {

        // Cấu hình upload
        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "auto");
        options.put("folder", folder); // Thư mục để lưu file
        options.put("public_id", fileName); // Tên file

        // Upload file lên Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

        // Lấy URL trả về
        return uploadResult.get("secure_url").toString();
    }

    public List<String> uploadManyFile(List<MultipartFile> files, String folder) throws IOException {
        List<String> urls = new ArrayList<>();
        int i = 1;
        for (MultipartFile file : files) {
            String fileName = "image_" + i;
            i++;
            String url = uploadFile(file, folder, fileName);
            urls.add(url);

        }
        return urls;
    }

}
