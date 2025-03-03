package com.example.dto;

import com.example.model.User;

import java.util.Date;

public class UserDTO {
    private String id;
    private String userName;
    private String userMail;
    private String userPhone;
    private User.Gender userGender;
    private String userAddress;
    private Date userBirth;
    private String imageURL;

    private String accountId;


    public UserDTO(String id, String userName, String userMail, String userPhone,
                   User.Gender userGender, String userAddress, Date userBirth,
                   String imageURL, String accountId) {
        this.id = id;
        this.userName = userName;
        this.userMail = userMail;
        this.userPhone = userPhone;
        this.userGender = userGender;
        this.userAddress = userAddress;
        this.userBirth = userBirth;
        this.imageURL = imageURL;
        this.accountId = accountId;
    }

    public boolean hasAccount() {
        return accountId != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public User.Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(User.Gender userGender) {
        this.userGender = userGender;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(Date userBirth) {
        this.userBirth = userBirth;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


}
