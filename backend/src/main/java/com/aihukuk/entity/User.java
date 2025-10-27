// 👤 User Entity - JavaScript User.js modelinin JPA karşılığı

package com.aihukuk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User Entity - Kullanıcı varlık sınıfı
 * 
 * JavaScript User.js sınıfının JPA karşılığı
 * MySQL 'users' tablosuna karşılık gelir
 * 
 * Özellikler:
 * - UUID tabanlı unique identifier
 * - Email tabanlı authentication
 * - BCrypt şifreli password
 * - Email doğrulama desteği
 * - Audit fields (created_at, updated_at)
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_uuid", columnList = "uuid"),
    @Index(name = "idx_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class User {

    // Primary Key - JavaScript'teki id field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique UUID - JavaScript'teki uuid field  
    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    private String uuid;

    // Full Name - JavaScript'teki full_name field
    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "Ad soyad gereklidir")
    @Size(min = 2, max = 100, message = "Ad soyad 2-100 karakter arasında olmalıdır")
    @JsonProperty("fullName")
    private String fullName;

    // Email - JavaScript'teki email field
    @Column(name = "email", nullable = false, unique = true, length = 255)
    @Email(message = "Geçerli bir email adresi giriniz")
    @NotBlank(message = "Email gereklidir")
    private String email;

    // Password Hash - JavaScript'teki password_hash field
    @Column(name = "password_hash", nullable = false, length = 255)
    @JsonIgnore // JSON response'larda password gösterme
    private String passwordHash;

    // Email Verification - JavaScript'teki email_verified field
    @Column(name = "email_verified", nullable = false)
    @JsonProperty("emailVerified")
    private Boolean emailVerified = false;

    // Email Verification Date - JavaScript'teki email_verified_at field
    @Column(name = "email_verified_at")
    @JsonProperty("emailVerifiedAt")
    private LocalDateTime emailVerifiedAt;

    // Created At - JavaScript'teki created_at field (otomatik)
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    // Updated At - JavaScript'teki updated_at field (otomatik)
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @JsonProperty("updatedAt") 
    private LocalDateTime updatedAt;

    // Last Login - JavaScript'teki last_login_at field
    @Column(name = "last_login_at")
    @JsonProperty("lastLoginAt")
    private LocalDateTime lastLoginAt;

    // Status - JavaScript'teki status field
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    // Status Enum - JavaScript'teki ENUM('active', 'suspended', 'deleted')
    public enum UserStatus {
        ACTIVE("active"),
        SUSPENDED("suspended"), 
        DELETED("deleted");

        private final String value;

        UserStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Default Constructor
    public User() {
        this.uuid = UUID.randomUUID().toString(); // Otomatik UUID üret
    }

    // Constructor with basic fields
    public User(String fullName, String email, String passwordHash) {
        this();
        this.fullName = fullName;
        this.email = email.toLowerCase(); // Email'i küçük harfe çevir
        this.passwordHash = passwordHash;
    }

    // Getters ve Setters

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
        this.email = email != null ? email.toLowerCase() : null;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        if (emailVerified != null && emailVerified && this.emailVerifiedAt == null) {
            this.emailVerifiedAt = LocalDateTime.now();
        }
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    // Helper Methods (JavaScript User.js'deki static metodların karşılığı)

    /**
     * Email doğrulamasını işaretle
     * JavaScript User.verifyEmail() metodunun karşılığı
     */
    public void markEmailAsVerified() {
        this.emailVerified = true;
        this.emailVerifiedAt = LocalDateTime.now();
    }

    /**
     * Son giriş zamanını güncelle
     * JavaScript User.updateLastLogin() metodunun karşılığı
     */
    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * Kullanıcı aktif mi kontrol et
     * JavaScript'teki status kontrolünün karşılığı
     */
    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    /**
     * Kullanıcı silinmiş mi kontrol et
     * JavaScript'teki status != "deleted" kontrolünün karşılığı
     */
    public boolean isNotDeleted() {
        return !UserStatus.DELETED.equals(this.status);
    }

    // toString, equals, hashCode

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return uuid != null && uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
