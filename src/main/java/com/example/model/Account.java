/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author admin
 */
@Document(collection = "account")
public class Account implements Serializable {

    @Id
    private String id;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("type")
    private Type type;
    
    @Field("status")
    private AccountStatus status;

    private Date expiredDay;

    
    public enum AccountStatus{
        ACTIVE,
        INACTIVE
    }
    // Default constructor
    public Account() {
        this.status = AccountStatus.ACTIVE;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1); // Cộng thêm 1 năm
        this.expiredDay = calendar.getTime(); // Lấy ngày mới
    }

    // Parameterized constructor
    public Account(String email, String password, Type type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        USER,
        COMPANY,
        ARTIST,
        ADMIN
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Date getExpiredDay() {
        return expiredDay;
    }

    public void setExpiredDay(Date expiredDay) {
        this.expiredDay = expiredDay;
    }
}