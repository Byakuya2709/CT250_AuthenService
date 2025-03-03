/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dto;

import com.example.model.Company;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
public class CompanyDTO {

    private String id;
    private String companyName;
    private String companyMail;
    private String companyPhone;
    private String companyAddress;
    private String logoURL;
    private Date publishDate;
    private String accountId;  // Chuyển thành String để tránh lỗi
    private Date expiredDay;

    public CompanyDTO(String id, String companyName, String companyMail, String companyPhone,
                      String companyAddress, String logoURL, Date publishDate,
                      String accountId, Date expiredDay) {
        this.id = id;
        this.companyName = companyName;
        this.companyMail = companyMail;
        this.companyPhone = companyPhone;
        this.companyAddress = companyAddress;
        this.logoURL = logoURL;
        this.publishDate = publishDate;
        this.accountId = accountId;
        this.expiredDay = expiredDay;
    }

    public Date getExpiredDay() {
        return expiredDay;
    }

    public void setExpiredDay(Date expiredDay) {
        this.expiredDay = expiredDay;
    }

    public CompanyDTO() {
    }

    public boolean hasAccount() {
        return accountId != null;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyMail() {
        return companyMail;
    }

    public void setCompanyMail(String companyMail) {
        this.companyMail = companyMail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

//    public class CompanyMapper {
//
//        public static CompanyDTO toDTO(Company company) {
//            if (company == null) {
//                return null;
//            }
//
//            CompanyDTO companyDTO = new CompanyDTO();
//            companyDTO.setId(company.getId());
//            companyDTO.setCompanyName(company.getCompanyName());
//            companyDTO.setCompanyMail(company.getCompanyMail());
//            companyDTO.setCompanyPhone(company.getCompanyPhone());
//            companyDTO.setCompanyAddress(company.getCompanyAddress());
//            companyDTO.setLogoURL(company.getLogoURL());
//            companyDTO.setPublishDate(company.getPublishDate());
//            companyDTO.setAccountId();
//
//            // Set the account ID
/// /            if (company.getAccount() != null) {
/// /                companyDTO.setAccountId(company.getAccount().getId());
/// /            }
//
//            return companyDTO;
//        }
//
//        public static Company toEntity(CompanyDTO companyDTO) {
//            if (companyDTO == null) {
//                return null;
//            }
//
//            Company company = new Company();
//            company.setId(companyDTO.getId());
//            company.setCompanyName(companyDTO.getCompanyName());
//            company.setCompanyMail(companyDTO.getCompanyMail());
//            company.setCompanyPhone(companyDTO.getCompanyPhone());
//            company.setCompanyAddress(companyDTO.getCompanyAddress());
//            company.setLogoURL(companyDTO.getLogoURL());
//            company.setPublishDate(companyDTO.getPublishDate());
//
//            // For simplicity, only set the IDs of the artists
//            // You would need to fetch the actual Artist objects from the database using their IDs
//            // Example:
//            // company.setArtists(artistRepository.findAllById(companyDTO.getArtistIds()));
//            // Set the account using the ID
//            // Example:
//            // company.setAccount(accountRepository.findById(companyDTO.getAccountId()).orElse(null));
//            return company;
//        }
//    }


