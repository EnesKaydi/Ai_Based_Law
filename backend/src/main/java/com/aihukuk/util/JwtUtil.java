// 🔐 JWT Utility - JavaScript middleware/auth.js JWT fonksiyonlarının Java karşılığı

package com.aihukuk.util;

import com.aihukuk.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Utility Sınıfı
 * 
 * JavaScript middleware/auth.js dosyasının JWT işlevlerinin Java karşılığı
 * 
 * Fonksiyonlar:
 * - generateTokens() -> JavaScript generateTokens()
 * - validateToken() -> JavaScript jwt.verify()
 * - extractEmail() -> Token'dan email çıkarma
 * - isTokenExpired() -> Token süre kontrolü
 */
@Component
public class JwtUtil {

    // JWT Configuration (application.yml'dan gelir)
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.refresh-secret}")
    private String jwtRefreshSecret;

    @Value("${jwt.access-expires-in:3600}")
    private Long accessTokenExpirationTime; // Saniye cinsinden (1 saat)

    @Value("${jwt.refresh-expires-in:2592000}")
    private Long refreshTokenExpirationTime; // Saniye cinsinden (30 gün)

    @Value("${jwt.issuer:ai-hukuk-api}")
    private String issuer;

    @Value("${jwt.audience:ai-hukuk-frontend}")
    private String audience;

    // Secret Key'leri oluştur
    private SecretKey getAccessTokenSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshTokenSigningKey() {
        byte[] keyBytes = jwtRefreshSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token üret
     * JavaScript generateTokens() metodunun access token kısmı
     * 
     * @param user Kullanıcı bilgileri
     * @return JWT Access Token
     */
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("uuid", user.getUuid());
        claims.put("email", user.getEmail());
        claims.put("emailVerified", user.getEmailVerified());
        claims.put("fullName", user.getFullName());

        return createToken(claims, user.getEmail(), accessTokenExpirationTime, getAccessTokenSigningKey());
    }

    /**
     * Refresh Token üret
     * JavaScript generateTokens() metodunun refresh token kısmı
     * 
     * @param user Kullanıcı bilgileri
     * @return JWT Refresh Token
     */
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("uuid", user.getUuid());
        claims.put("email", user.getEmail());

        return createToken(claims, user.getEmail(), refreshTokenExpirationTime, getRefreshTokenSigningKey());
    }

    /**
     * Token oluştur (ortak metod)
     * 
     * @param claims Token içeriği
     * @param subject Subject (email)
     * @param expirationTime Geçerlilik süresi (saniye)
     * @param signingKey İmzalama anahtarı
     * @return JWT Token
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime, SecretKey signingKey) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (expirationTime * 1000));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setAudience(audience)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Token'dan email çıkar
     * JavaScript jwt.verify() sonrasındaki email çıkarma
     * 
     * @param token JWT Token
     * @return Email adresi
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Token'dan User ID çıkar
     * 
     * @param token JWT Token
     * @return User ID
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userIdClaim = claims.get("userId");
        
        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).longValue();
        }
        return null;
    }

    /**
     * Token'dan UUID çıkar
     * 
     * @param token JWT Token
     * @return User UUID
     */
    public String extractUuid(String token) {
        return (String) extractAllClaims(token).get("uuid");
    }

    /**
     * Token geçerlilik tarihi
     * 
     * @param token JWT Token
     * @return Geçerlilik tarihi
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Token'dan claim çıkar
     * 
     * @param token JWT Token
     * @param claimsResolver Claim çıkarma fonksiyonu
     * @return Claim değeri
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Tüm claim'leri çıkar
     * 
     * @param token JWT Token
     * @return Claims
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getAccessTokenSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz JWT token formatı", e);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("JWT token süresi dolmuş", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("JWT token imzası geçersiz", e);
        }
    }

    /**
     * Refresh token'dan claim'leri çıkar
     * 
     * @param refreshToken Refresh Token
     * @return Claims
     */
    private Claims extractRefreshTokenClaims(String refreshToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getRefreshTokenSigningKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Geçersiz refresh token", e);
        }
    }

    /**
     * Token süresi dolmuş mu kontrol et
     * 
     * @param token JWT Token
     * @return Süre dolmuşsa true
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Access token geçerli mi kontrol et
     * JavaScript jwt.verify() metodunun karşılığı
     * 
     * @param token JWT Token
     * @param userDetails Kullanıcı detayları
     * @return Token geçerliyse true
     */
    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        try {
            final String email = extractEmail(token);
            return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Access token geçerli mi kontrol et (UserDetails olmadan)
     * 
     * @param token JWT Token
     * @return Token geçerliyse true
     */
    public Boolean validateAccessToken(String token) {
        try {
            extractAllClaims(token); // Bu, token'ı parse eder ve doğrular
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Refresh token geçerli mi kontrol et
     * JavaScript verifyRefreshToken() metodunun karşılığı
     * 
     * @param refreshToken Refresh Token
     * @return Token geçerliyse true
     */
    public Boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = extractRefreshTokenClaims(refreshToken);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Refresh token'dan email çıkar
     * 
     * @param refreshToken Refresh Token
     * @return Email adresi
     */
    public String extractEmailFromRefreshToken(String refreshToken) {
        Claims claims = extractRefreshTokenClaims(refreshToken);
        return claims.getSubject();
    }

    /**
     * Refresh token'dan User ID çıkar
     * 
     * @param refreshToken Refresh Token
     * @return User ID
     */
    public Long extractUserIdFromRefreshToken(String refreshToken) {
        Claims claims = extractRefreshTokenClaims(refreshToken);
        Object userIdClaim = claims.get("userId");
        
        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).longValue();
        }
        return null;
    }

    /**
     * Token'ı Bearer prefix ile birlikte al
     * JavaScript'teki authHeader.split(' ')[1] işleminin yardımcısı
     * 
     * @param authorizationHeader Authorization header
     * @return JWT token veya null
     */
    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " kısmını at
        }
        return null;
    }

    /**
     * Token bilgilerini map olarak döndür
     * JavaScript token payload'ının karşılığı
     * 
     * @param token JWT Token
     * @return Token bilgileri
     */
    public Map<String, Object> getTokenInfo(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> tokenInfo = new HashMap<>();
        
        tokenInfo.put("userId", claims.get("userId"));
        tokenInfo.put("uuid", claims.get("uuid"));
        tokenInfo.put("email", claims.getSubject());
        tokenInfo.put("emailVerified", claims.get("emailVerified"));
        tokenInfo.put("fullName", claims.get("fullName"));
        tokenInfo.put("issuedAt", claims.getIssuedAt());
        tokenInfo.put("expiresAt", claims.getExpiration());
        tokenInfo.put("issuer", claims.getIssuer());
        tokenInfo.put("audience", claims.getAudience());
        
        return tokenInfo;
    }
}
