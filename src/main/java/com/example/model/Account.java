/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import java.io.Serializable;
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

    // Default constructor
    public Account() {
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
}