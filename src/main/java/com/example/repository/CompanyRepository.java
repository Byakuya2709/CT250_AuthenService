/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repository;

import com.example.dto.CompanyDTO;
import com.example.model.Company;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author admin
 */
@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    // Bạn có thể thêm các phương thức tìm kiếm theo nhu cầu
    Optional<Company> findByAccountId(ObjectId accountId);

    @Override
    Page<Company> findAll(Pageable pageable);




//    @Query(value = "{}", fields = "{ 'id': 1, 'companyName': 1, 'companyMail': 1, 'companyPhone': 1, 'companyAddress': 1, 'logoURL': 1, 'publishDate': 1, 'account.id': 1 }")
//    Page<CompanyDTO> findAllCompaniesWithAccountInfo(Pageable pageable);

    void deleteByAccountId(String accountId);
}