/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@Document(collection = "users")
public class User implements Serializable {

    @Id
    private String id;
    private String userName;
    private String userMail;
    private String userPhone;
    private Gender userGender;
    private String userAddress;
    private Date userBirth;
    private String imageURL;

    @JsonSerialize(using = ToStringSerializer.class) // Chuyển ObjectId thành String khi serialize JSON
    private ObjectId accountId;
    public enum Gender {
        MALE,
        FEMALE
    }

    public User() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserBirth(Date userBirth) {
        this.userBirth = userBirth;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ObjectId getAccountId() {
        return accountId;
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public Date getUserBirth() {
        return userBirth;
    }

    public String getImageURL() {
        return imageURL;
    }

}
