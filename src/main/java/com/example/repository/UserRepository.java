/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repository;

import com.example.dto.CompanyDTO;
import com.example.dto.UserDTO;
import com.example.model.Company;
import com.example.model.User;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author admin
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByAccountId(ObjectId accountId);

    @Override
    Page<User> findAll(Pageable pageable);




    @Query(value = "{}", fields = "{ 'id': 1, 'userName': 1, 'userMail': 1, 'userPhone': 1, 'userGender': 1, 'userAddress': 1, 'userBirth': 1, 'imageURL': 1, 'account.id': 1 }")
    Page<UserDTO> findAllUsersWithAccountInfo(Pageable pageable);

}
