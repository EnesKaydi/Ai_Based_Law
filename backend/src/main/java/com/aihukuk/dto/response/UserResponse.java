// 👤 User Response DTO - JavaScript User model response karşılığı

package com.aihukuk.dto.response;

import com.aihukuk.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Kullanıcı Response DTO
 * 
 * JavaScript User.toJSON() metodunun karşılığı
 * Şifre hash'i ve diğer hassas bilgileri hariç tutar
 */
public class UserResponse {

    private Long id;
    private String uuid;
    
    @JsonProperty("fullName")
    private String fullName;
    
    private String email;
    
    @JsonProperty("emailVerified")
    private Boolean emailVerified;
    
    @JsonProperty("emailVerifiedAt")
    private LocalDateTime emailVerifiedAt;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    @JsonProperty("lastLoginAt")
    private LocalDateTime lastLoginAt;
    
    private String status;

    // Default Constructor
    public UserResponse() {}

    // Constructor from User entity (JavaScript toJSON() karşılığı)
    public UserResponse(User user) {
        this.id = user.getId();
        this.uuid = user.getUuid();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.emailVerified = user.getEmailVerified();
        this.emailVerifiedAt = user.getEmailVerifiedAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.lastLoginAt = user.getLastLoginAt();
        this.status = user.getStatus() != null ? user.getStatus().getValue() : null;
    }

    // Static Factory Method
    public static UserResponse from(User user) {
        return new UserResponse(user);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", status='" + status + '\'' +
                '}';
    }
}
