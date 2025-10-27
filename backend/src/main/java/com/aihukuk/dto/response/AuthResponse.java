// 🔐 Auth Response DTO - JavaScript auth endpoint response karşılığı

package com.aihukuk.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication Response DTO
 * 
 * JavaScript auth.js register/login endpoint'lerinin response formatı
 * Format: { user: UserResponse, tokens: TokenInfo, emailVerificationRequired: boolean }
 */
public class AuthResponse {

    private UserResponse user;
    private TokenInfo tokens;
    
    @JsonProperty("emailVerificationRequired")
    private boolean emailVerificationRequired;

    // Default Constructor
    public AuthResponse() {}

    // Constructor with all fields
    public AuthResponse(UserResponse user, TokenInfo tokens, boolean emailVerificationRequired) {
        this.user = user;
        this.tokens = tokens;
        this.emailVerificationRequired = emailVerificationRequired;
    }

    // Constructor (JavaScript pattern'i takip ediyor)
    public AuthResponse(UserResponse user, String accessToken, String refreshToken) {
        this.user = user;
        this.tokens = new TokenInfo(accessToken, refreshToken);
        this.emailVerificationRequired = !user.getEmailVerified();
    }

    // Token Information Inner Class (JavaScript tokens objesi)
    public static class TokenInfo {
        
        @JsonProperty("accessToken")
        private String accessToken;
        
        @JsonProperty("refreshToken") 
        private String refreshToken;
        
        @JsonProperty("expiresIn")
        private int expiresIn = 3600; // 1 hour (JavaScript'teki gibi)
        
        @JsonProperty("tokenType")
        private String tokenType = "Bearer";

        // Default Constructor
        public TokenInfo() {}

        // Constructor
        public TokenInfo(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        // Constructor with expiration
        public TokenInfo(String accessToken, String refreshToken, int expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
        }

        // Getters and Setters
        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        @Override
        public String toString() {
            return "TokenInfo{" +
                    "tokenType='" + tokenType + '\'' +
                    ", expiresIn=" + expiresIn +
                    '}';
        }
    }

    // Getters and Setters

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public TokenInfo getTokens() {
        return tokens;
    }

    public void setTokens(TokenInfo tokens) {
        this.tokens = tokens;
    }

    public boolean isEmailVerificationRequired() {
        return emailVerificationRequired;
    }

    public void setEmailVerificationRequired(boolean emailVerificationRequired) {
        this.emailVerificationRequired = emailVerificationRequired;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "user=" + user +
                ", tokens=" + tokens +
                ", emailVerificationRequired=" + emailVerificationRequired +
                '}';
    }
}
