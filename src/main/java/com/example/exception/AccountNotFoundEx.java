/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.exception;

/**
 *
 * @author admin
 */
public class AccountNotFoundEx extends RuntimeException{
     public AccountNotFoundEx() {
        super("Tài khoản không tồn tại");
    }
}
