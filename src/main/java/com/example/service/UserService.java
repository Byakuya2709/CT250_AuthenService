/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.service;

import com.example.dto.UserDTO;
import com.example.exception.CompanyNotFoundEx;
import com.example.exception.UserNotFoundException;
import com.example.model.Account;
import com.example.model.Company;
import com.example.model.User;
import com.example.repository.AccountRepository;
import com.example.repository.UserRepository;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        user.setAccountId(new ObjectId(accountId));  // Liên kết tài khoản với người dùng
        return userRepository.save(user);  // Lưu người dùng vào cơ sở dữ liệu
    }

    public User findUserById(String accountId) {
        Optional<User> user = userRepository.findByAccountId(new ObjectId(accountId));
        return user.orElseThrow(() -> new UserNotFoundException("No user found for account ID: " + accountId));

    }

    public User update(UserDTO req){
        Optional<User> user = userRepository.findById(req.getId());
        if (user.isEmpty()) {
            throw new UserNotFoundException("Người dùng không tồn tại!!!");
        }
        User existingUser = user.get();
        existingUser.setUserName(req.getUserName());
        existingUser.setUserAddress(req.getUserAddress());
        existingUser.setUserGender(req.getUserGender());
        existingUser.setUserPhone(req.getUserPhone());
        existingUser.setImageURL(req.getImageURL());


        return userRepository.save(existingUser);
    }


    public Page<User> getAllUserWithPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
