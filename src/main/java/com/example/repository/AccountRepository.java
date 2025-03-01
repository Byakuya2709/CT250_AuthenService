/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repository;

import com.example.model.Account;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

/**
 * @author admin
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email); // Renamed to follow standard convention

    boolean existsById(String id);

    @Query(value = "{ 'type' : { $ne: 'ADMIN' } }", fields = "{ 'password': 0 }")
    Page<Account> findAll(Pageable pageable);

    @Query(value = "{ 'type' : { $ne: 'ADMIN' } }", count = true)
    Long countNonAdminAccounts();

    @Query(value = "{ 'status' : 'INACTIVE' }", count = true)
    Long countInactiveAccounts();

}
