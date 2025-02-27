/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint để tạo người dùng mới
    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody User user, @RequestParam String accountId) {
        try {
            User newUser = userService.createUser(user, accountId);
            return ResponseHandler.resBuilder("Tạo thông tin người dùng thành công.", HttpStatus.CREATED, newUser);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi tạo thông tin người dùng.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getUserById(@PathVariable("accountId") String accountId) {
        try {
            User res = userService.findUserById(accountId);
            return ResponseHandler.resBuilder("Lấy thông tin người dùng thành công.", HttpStatus.OK, res);
        } catch (RuntimeException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseHandler.resBuilder("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
