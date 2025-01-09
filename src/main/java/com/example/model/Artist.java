package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

    @Document(collection = "artists")
    public class Artist implements Serializable {

        @Id
        private String id;
        private String artistName;
        private String contactMail;
        private String contactPhone;
        private User.Gender artistGender;
        private Date artistBirth;
        private String imageURL;
        private String artistBio;

        // Công ty mà nghệ sĩ này thuộc về
        @DBRef
        private Company company; // Mỗi nghệ sĩ chỉ thuộc về một công ty duy nhất

        @DBRef
        @JsonIgnore
        private Account account;

    // Getters và Setters
    public String getId() {
        return id;
    }

    public String getArtistBio() {
        return artistBio;
    }

    public void setArtistBio(String artistBio) {
        this.artistBio = artistBio;
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
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
