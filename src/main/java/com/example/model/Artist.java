/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author admin
 */
@Document(collection = "artists")
public class Artist implements Serializable{

    @Id
    private String id;
    private String artistName;
    private String ContactMail;
    private String ContactPhone;
    private User.Gender artistGender;
    private Date artistBirth;
    private String imageURL;
    @DBRef
    private Account account;

    public Artist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getContactMail() {
        return ContactMail;
    }

    public void setContactMail(String ContactMail) {
        this.ContactMail = ContactMail;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String ContactPhone) {
        this.ContactPhone = ContactPhone;
    }

    public User.Gender getArtistGender() {
        return artistGender;
    }

    public void setArtistGender(User.Gender artistGender) {
        this.artistGender = artistGender;
    }

    public Date getArtistBirth() {
        return artistBirth;
    }

    public void setArtistBirth(Date artistBirth) {
        this.artistBirth = artistBirth;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
