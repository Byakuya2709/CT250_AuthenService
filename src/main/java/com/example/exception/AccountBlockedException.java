/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.exception;

/**
 *
 * @author ADMIN
 */
public class AccountBlockedException extends RuntimeException{
     public AccountBlockedException(String message) {
        super(message);
    }
}
