// 🔐 Authentication Controller - JavaScript auth.js routes'ının Spring karşılığı

package com.aihukuk.controller;

import com.aihukuk.dto.request.LoginRequest;
import com.aihukuk.dto.request.RegisterRequest;
import com.aihukuk.dto.response.ApiResponse;
import com.aihukuk.dto.response.AuthResponse;
import com.aihukuk.dto.response.UserResponse;
import com.aihukuk.entity.User;
import com.aihukuk.service.UserService;
import com.aihukuk.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Authentication Controller
 * 
 * JavaScript auth.js router'ının Spring Boot @RestController karşılığı
 * 
 * Endpoint'ler:
 * - POST /v1/auth/register -> JavaScript router.post('/register')
 * - POST /v1/auth/login -> JavaScript router.post('/login')
 * - POST /v1/auth/refresh -> JavaScript router.post('/refresh')
 * - GET /v1/auth/profile -> JavaScript router.get('/profile')
 * - POST /v1/auth/logout -> JavaScript router.post('/logout')
 */
@RestController
@RequestMapping("/v1/auth")
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Kullanıcı kayıt endpoint'i
     * JavaScript router.post('/register') metodunun karşılığı
     * 
     * @param request Kayıt isteği
     * @return API Response with user and tokens
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Kullanıcı oluştur (JavaScript User.create karşılığı)
            User user = userService.createUser(
                request.getFullName(),
                request.getEmail(),
                request.getPassword()
            );

            // JWT token'ları üret (JavaScript generateTokens karşılığı)
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // Response oluştur (JavaScript response format'ı)
            UserResponse userResponse = new UserResponse(user);
            AuthResponse authResponse = new AuthResponse(userResponse, accessToken, refreshToken);

            // JavaScript: res.status(201).json({ success: true, message: "...", data: ... })
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Kullanici basariyla olusturuldu", authResponse));

            // TODO: Email verification gönder (JavaScript'teki comment'te belirtilmiş)
            // console.log(`📧 Email verification needed for: ${email}`);

        } catch (IllegalArgumentException e) {
            // Email duplicate error (JavaScript'teki duplicate check)
            if (e.getMessage().contains("email adresi zaten")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(e.getMessage(), "DUPLICATE_EMAIL"));
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage(), "VALIDATION_ERROR"));

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Kayıt işlemi sırasında hata oluştu", "REGISTER_ERROR"));
        }
    }

    /**
     * Kullanıcı giriş endpoint'i
     * JavaScript router.post('/login') metodunun karşılığı
     * 
     * @param request Giriş isteği
     * @param httpRequest HTTP request (IP bilgisi için)
     * @return API Response with user and tokens
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
                                                          HttpServletRequest httpRequest) {
        try {
            // Kullanıcı kimlik doğrulaması (JavaScript User.authenticate karşılığı)
            User user = userService.authenticateUser(request.getEmail(), request.getPassword());
            
            if (user == null) {
                // JavaScript: return res.status(401).json({ success: false, message: "Email veya şifre hatalı" })
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Email veya şifre hatalı", "INVALID_CREDENTIALS"));
            }

            // Email doğrulaması kontrolü (JavaScript'teki optional check)
            if (!user.getEmailVerified()) {
                System.out.println("⚠️ User " + user.getEmail() + " logged in without email verification");
            }

            // JWT token'ları üret (JavaScript generateTokens karşılığı)
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // Başarılı giriş log'u (JavaScript console.log karşılığı)
            String clientIp = httpRequest.getRemoteAddr();
            System.out.println("✅ User logged in: " + user.getEmail() + " from " + clientIp);

            // Response oluştur (JavaScript response format'ı)
            UserResponse userResponse = new UserResponse(user);
            AuthResponse authResponse = new AuthResponse(userResponse, accessToken, refreshToken);

            // JavaScript: res.json({ success: true, message: "Giriş başarılı", data: ... })
            return ResponseEntity.ok(
                    ApiResponse.success("Giriş başarılı", authResponse)
            );

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Giriş işlemi sırasında hata oluştu", "LOGIN_ERROR"));
        }
    }

    /**
     * Token yenileme endpoint'i
     * JavaScript router.post('/refresh') metodunun karşılığı
     * 
     * @param request Refresh token içeren request
     * @return Yeni access token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refresh(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");

            if (refreshToken == null || refreshToken.isEmpty()) {
                // JavaScript: return res.status(400).json({ success: false, message: "Refresh token gerekli" })
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Refresh token gerekli", "REFRESH_TOKEN_REQUIRED"));
            }

            // Refresh token doğrula (JavaScript verifyRefreshToken karşılığı)
            if (!jwtUtil.validateRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Geçersiz refresh token", "INVALID_REFRESH_TOKEN"));
            }

            // User ID'yi çıkar ve kullanıcıyı bul
            Long userId = jwtUtil.extractUserIdFromRefreshToken(refreshToken);
            User user = userService.findById(userId);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Geçersiz refresh token", "INVALID_REFRESH_TOKEN"));
            }

            // Yeni access token üret (JavaScript generateTokens karşılığı)
            String newAccessToken = jwtUtil.generateAccessToken(user);

            // JavaScript response format'ı
            Map<String, Object> tokenData = Map.of(
                "accessToken", newAccessToken,
                "expiresIn", 3600,
                "tokenType", "Bearer"
            );

            return ResponseEntity.ok(
                    ApiResponse.success("Token yenilendi", tokenData)
            );

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Geçersiz refresh token", "INVALID_REFRESH_TOKEN"));
        }
    }

    /**
     * Kullanıcı profil bilgileri endpoint'i
     * JavaScript router.get('/profile') metodunun karşılığı
     * 
     * @param authentication Spring Security authentication
     * @return Kullanıcı profil bilgileri
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfile(Authentication authentication) {
        try {
            // Authentication'dan email al (JWT filter'ından gelir)
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Kullanıcı bulunamadı", "USER_NOT_FOUND"));
            }

            // JavaScript: res.json({ success: true, data: { user: req.user.toJSON() } })
            UserResponse userResponse = new UserResponse(user);
            Map<String, Object> profileData = Map.of("user", userResponse);

            return ResponseEntity.ok(
                    ApiResponse.success("Profil bilgileri alındı", profileData)
            );

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Profil bilgileri alınamadı", "PROFILE_ERROR"));
        }
    }

    /**
     * Çıkış endpoint'i
     * JavaScript router.post('/logout') metodunun karşılığı
     * 
     * @param authentication Spring Security authentication
     * @return Çıkış onayı
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        try {
            // JavaScript: console.log(`👋 User logged out: ${req.user.email}`);
            String email = authentication.getName();
            System.out.println("👋 User logged out: " + email);

            // TODO: Token blacklisting implement et (JavaScript comment'te belirtilmiş)
            
            // JavaScript: res.json({ success: true, message: "Başarıyla çıkış yapıldı" })
            return ResponseEntity.ok(
                    ApiResponse.success("Başarıyla çıkış yapıldı")
            );

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Çıkış işlemi sırasında hata oluştu", "LOGOUT_ERROR"));
        }
    }

    /**
     * Test endpoint (opsiyonel - development için)
     * JavaScript'te yoktu, debug için eklendi
     * 
     * @return API durumu
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<Map<String, String>>> test() {
        Map<String, String> testData = Map.of(
            "message", "Auth Controller çalışıyor",
            "version", "1.0.0",
            "timestamp", java.time.LocalDateTime.now().toString()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Test başarılı", testData)
        );
    }
}
