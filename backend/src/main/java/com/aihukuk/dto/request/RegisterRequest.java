// 📝 Register Request DTO - JavaScript register endpoint'inin request karşılığı

package com.aihukuk.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

/**
 * Kayıt İsteği DTO
 * 
 * JavaScript auth.js register endpoint'inin request body'si
 * Validation kuralları JavaScript registerValidation ile aynı
 */
public class RegisterRequest {

    @NotBlank(message = "Ad soyad gereklidir")
    @Size(min = 2, max = 100, message = "Ad soyad 2-100 karakter arasında olmalıdır")
    @Pattern(regexp = "^[a-zA-ZçğıöşüÇĞIİÖŞÜ\\s]+$", 
             message = "Ad soyad sadece harf ve boşluk içerebilir")
    @JsonProperty("fullName")
    private String fullName;

    @NotBlank(message = "Email gereklidir")
    @Email(message = "Geçerli bir email adresi girin")
    private String email;

    @NotBlank(message = "Şifre gereklidir")
    @Size(min = 8, message = "Şifre en az 8 karakter olmalıdır")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "Şifre en az 1 küçük harf, 1 büyük harf, 1 rakam ve 1 özel karakter içermelidir")
    private String password;

    @NotBlank(message = "Şifre tekrarı gereklidir")
    @JsonProperty("confirmPassword")
    private String confirmPassword;

    @AssertTrue(message = "Kullanım şartlarını kabul etmelisiniz")
    @JsonProperty("termsAccepted")
    private boolean termsAccepted;

    // Default Constructor
    public RegisterRequest() {}

    // Constructor with all fields
    public RegisterRequest(String fullName, String email, String password, 
                          String confirmPassword, boolean termsAccepted) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.termsAccepted = termsAccepted;
    }

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    /**
     * Şifre doğrulama kontrolü
     * JavaScript validation'daki confirmPassword kontrolü
     */
    @AssertTrue(message = "Şifre tekrarı eşleşmiyor")
    public boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", termsAccepted=" + termsAccepted +
                '}';
    }
}
