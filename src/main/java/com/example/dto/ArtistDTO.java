/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dto;

import com.example.model.Artist;
import java.util.Date;

/**
 *
 * @author admin
 */
public class ArtistDTO {

    private String id;
    private String artistName;
    private String contactMail;
    private String contactPhone;
    private Gender artistGender;
    private Date artistBirth;
    private String imageURL;
    private String artistBio;
    private String companyId; // Just an ID for the company
    private String accountId; // Just an ID for the account

    // Getters and Setters
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

    public Gender getArtistGender() {
        return artistGender;
    }

    public void setArtistGender(Gender artistGender) {
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

    public String getArtistBio() {
        return artistBio;
    }

    public void setArtistBio(String artistBio) {
        this.artistBio = artistBio;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public class ArtistMapper {

        public static ArtistDTO toDTO(Artist artist) {
            if (artist == null) {
                return null;
            }

            ArtistDTO artistDTO = new ArtistDTO();
            artistDTO.setId(artist.getId());
            artistDTO.setArtistName(artist.getArtistName());
            artistDTO.setContactMail(artist.getContactMail());
            artistDTO.setContactPhone(artist.getContactPhone());
            artistDTO.setArtistGender(Gender.valueOf(artist.getArtistGender().toString()));
            artistDTO.setArtistBirth(artist.getArtistBirth());
            artistDTO.setImageURL(artist.getImageURL());
            artistDTO.setArtistBio(artist.getArtistBio());

            // Set the company and account IDs instead of embedding full objects
            if (artist.getCompany() != null) {
                artistDTO.setCompanyId(artist.getCompany().getId());
            }
            if (artist.getAccount() != null) {
                artistDTO.setAccountId(artist.getAccount().getId());
            }

            return artistDTO;
        }

        public static Artist toEntity(ArtistDTO artistDTO) {
            if (artistDTO == null) {
                return null;
            }

            Artist artist = new Artist();
            artist.setId(artistDTO.getId());
            artist.setArtistName(artistDTO.getArtistName());
            artist.setContactMail(artistDTO.getContactMail());
            artist.setContactPhone(artistDTO.getContactPhone());
//            artist.setArtistGender());
            artist.setArtistBirth(artistDTO.getArtistBirth());
            artist.setImageURL(artistDTO.getImageURL());
            artist.setArtistBio(artistDTO.getArtistBio());

            // Set the company and account objects if you want to fetch them from the DB
            // For example, using their IDs, you would fetch from MongoDB or another data source
            // Example:
            // artist.setCompany(companyRepository.findById(artistDTO.getCompanyId()).orElse(null));
            // artist.setAccount(accountRepository.findById(artistDTO.getAccountId()).orElse(null));
            return artist;
        }
    }

}
