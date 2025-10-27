// 🔐 Login Request DTO - JavaScript login endpoint'inin request karşılığı

package com.aihukuk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Giriş İsteği DTO
 * 
 * JavaScript auth.js login endpoint'inin request body'si
 * Validation kuralları JavaScript loginValidation ile aynı
 */
public class LoginRequest {

    @NotBlank(message = "Email gereklidir")
    @Email(message = "Geçerli bir email adresi girin")
    private String email;

    @NotBlank(message = "Şifre gereklidir")
    private String password;

    @JsonProperty("rememberMe")
    private boolean rememberMe = false; // JavaScript'teki rememberMe default false

    // Default Constructor
    public LoginRequest() {}

    // Constructor with required fields
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Constructor with all fields
    public LoginRequest(String email, String password, boolean rememberMe) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    // Getters and Setters

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

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }
}
