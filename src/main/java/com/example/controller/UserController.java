/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.dto.TicketResponse;
import com.example.dto.UserDTO;
import com.example.exception.CompanyNotFoundEx;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import com.example.service.EmailService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private EmailService emailService;

    @PostMapping("/tickets/email")
    public ResponseEntity<?> sendMailTicket(@RequestBody TicketResponse ticket) {
        try {
            emailService.sendTicketEmail(ticket.getUserMail(),ticket);
            return ResponseHandler.resBuilder("Cập nhật thông tin người dùng thành công.", HttpStatus.OK, null);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.resBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception ex) {
            return ResponseHandler.resBuilder("Có lỗi xảy ra khi tạo thông tin người dùng.", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

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
    @PatchMapping("")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user) {
        try {
            User updateUser = userService.update(user);
            return ResponseHandler.resBuilder("Cập nhật thông tin người dùng thành công.", HttpStatus.CREATED, updateUser);
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
        } catch (UserNotFoundException ex) {
            return ResponseHandler.resBuilder("Thông tin công ty chưa được tạo", HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseHandler.resBuilder("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
    @GetMapping("/info/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable("userId") String userId) {
        try {
            User res = userService.getUserSummaryById(userId);
            return ResponseHandler.resBuilder("Lấy thông tin người dùng thành công.", HttpStatus.OK, res);
        } catch (UserNotFoundException ex) {
            return ResponseHandler.resBuilder("Thông tin công ty chưa được tạo", HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseHandler.resBuilder("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


}
