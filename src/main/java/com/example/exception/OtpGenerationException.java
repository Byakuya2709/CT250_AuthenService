/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.authen.exception;

/**
 *
 * @author admin
 */
public class OtpGenerationException extends RuntimeException {
    public OtpGenerationException(String message) {
        super(message);
    }
}