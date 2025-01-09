/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.model.Account;
import com.example.model.User;
import com.example.repository.AccountRepository;
import com.example.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Tạo người dùng mới và liên kết với tài khoản
    public User createUser(User user, String accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không hợp lệ");
        }
        System.out.println(user.getImageURL());
        Account account = accountOptional.get();
        user.setAccount(account);  // Liên kết tài khoản với người dùng
        return userRepository.save(user);  // Lưu người dùng vào cơ sở dữ liệu
    }

    public User findUserById(String accountId) {
        Optional<User> user = userRepository.findByAccount_Id(accountId);
        return user.orElseThrow(() -> new RuntimeException("No user found for account ID: " + accountId));

    }
}
