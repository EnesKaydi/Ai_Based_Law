// 🚨 Global Exception Handler - JavaScript errorHandler.js middleware'inin karşılığı

package com.aihukuk.exception;

import com.aihukuk.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 * 
 * JavaScript errorHandler.js middleware'inin Spring Boot @ControllerAdvice karşılığı
 * 
 * Yakalanan hatalar:
 * - Validation errors -> JavaScript ValidationError
 * - Database errors -> JavaScript MySQL errors (ER_DUP_ENTRY, vb.)
 * - Authentication errors -> JavaScript JWT errors
 * - Generic exceptions -> JavaScript generic error handler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${spring.profiles.active:development}")
    private String activeProfile;

    /**
     * Validation Exception Handler
     * JavaScript validation error'ların karşılığı
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        logError("Validation error", ex, request);

        // JavaScript validation error format'ı
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            
            if (!errors.containsKey(fieldName)) {
                errors.put(fieldName, new java.util.ArrayList<String>());
            }
            ((java.util.List<String>) errors.get(fieldName)).add(errorMessage);
        });

        Map<String, Object> errorData = Map.of("errors", errors);

        // JavaScript: error.message = 'Veri doğrulama hatası'; error.code = 'VALIDATION_ERROR';
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Doğrulama hatası", "VALIDATION_ERROR", errorData));
    }

    /**
     * MySQL Duplicate Entry Exception Handler
     * JavaScript ER_DUP_ENTRY error'ının karşılığı
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        logError("Data integrity violation", ex, request);

        String message = ex.getMessage();
        
        // JavaScript: if (err.code === 'ER_DUP_ENTRY') 
        if (message != null && message.toLowerCase().contains("duplicate")) {
            
            // JavaScript: if (err.message.includes('email'))
            if (message.toLowerCase().contains("email")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("Bu email adresi zaten kullanılıyor", "DUPLICATE_EMAIL"));
            }
            
            // JavaScript: error.message = 'Bu veri zaten mevcut'; error.code = 'DUPLICATE_ENTRY';
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Bu veri zaten mevcut", "DUPLICATE_ENTRY"));
        }

        // JavaScript: if (err.code === 'ER_NO_REFERENCED_ROW_2')
        if (message != null && message.toLowerCase().contains("foreign key")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("İlişkili veri bulunamadı", "INVALID_REFERENCE"));
        }

        // Generic data integrity error
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Veri bütünlüğü hatası", "DATA_INTEGRITY_ERROR"));
    }

    /**
     * SQL Constraint Violation Exception Handler
     * JavaScript MySQL constraint error'ların karşılığı
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleSQLIntegrityConstraintViolation(
            SQLIntegrityConstraintViolationException ex, HttpServletRequest request) {
        
        logError("SQL constraint violation", ex, request);

        // JavaScript MySQL error handling
        if (ex.getMessage().contains("Duplicate entry")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Bu veri zaten mevcut", "DUPLICATE_ENTRY"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Veritabanı kısıtlama hatası", "SQL_CONSTRAINT_ERROR"));
    }

    /**
     * Authentication Exception Handler
     * JavaScript JWT error'ların karşılığı
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        
        logError("Authentication error", ex, request);

        // JavaScript: if (err.message && err.message.includes('JWT'))
        if (ex.getMessage().contains("JWT") || ex instanceof BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token doğrulama hatası", "TOKEN_ERROR"));
        }

        // JavaScript: error.message = 'Kimlik dogrulama gerekli'; error.code = 'AUTHENTICATION_ERROR';
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Kimlik doğrulama gerekli", "AUTHENTICATION_ERROR"));
    }

    /**
     * Access Denied Exception Handler
     * JavaScript 403 error'ların karşılığı
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        
        logError("Access denied", ex, request);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Erişim izni yok", "ACCESS_DENIED"));
    }

    /**
     * Authentication Credentials Not Found Exception
     * JavaScript missing token error'ının karşılığı
     */
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationCredentialsNotFound(
            AuthenticationCredentialsNotFoundException ex, HttpServletRequest request) {
        
        logError("Authentication credentials not found", ex, request);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Erişim tokeni gerekli", "AUTH_001"));
    }

    /**
     * Constraint Violation Exception Handler
     * Bean validation error'ların karşılığı
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        logError("Constraint violation", ex, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Geçersiz veri formatı", "INVALID_FORMAT"));
    }

    /**
     * Method Argument Type Mismatch Exception
     * JavaScript CastError'ların karşılığı
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        logError("Method argument type mismatch", ex, request);

        // JavaScript: if (err.name === 'CastError')
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Geçersiz veri formatı", "INVALID_FORMAT"));
    }

    /**
     * HTTP Message Not Readable Exception
     * JSON parsing error'ların karşılığı
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        logError("HTTP message not readable", ex, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Geçersiz JSON formatı", "INVALID_JSON"));
    }

    /**
     * Illegal Argument Exception Handler
     * JavaScript application error'ların karşılığı
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        logError("Illegal argument", ex, request);

        // JavaScript: if (err.statusCode) 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), "APPLICATION_ERROR"));
    }

    /**
     * Generic Exception Handler
     * JavaScript generic error handler'ının karşılığı
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        logError("Generic error", ex, request);

        // JavaScript: error.message = 'Sunucu hatası oluştu'; error.code = 'SERVER_ERROR';
        ApiResponse<Map<String, Object>> response = ApiResponse.error("Sunucu hatası oluştu", "SERVER_ERROR");

        // JavaScript: if (process.env.NODE_ENV === 'development')
        if ("development".equals(activeProfile)) {
            Map<String, Object> debugInfo = Map.of(
                "message", ex.getMessage(),
                "type", ex.getClass().getSimpleName(),
                "timestamp", LocalDateTime.now()
            );
            response.setData(debugInfo);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Error logging helper method
     * JavaScript console.error karşılığı
     */
    private void logError(String errorType, Exception ex, HttpServletRequest request) {
        // JavaScript error logging format'ının karşılığı
        logger.error("Error occurred: {} - {} - URL: {} - Method: {} - IP: {} - User-Agent: {} - Timestamp: {}",
                errorType,
                ex.getMessage(),
                request.getRequestURI(),
                request.getMethod(),
                getClientIP(request),
                request.getHeader("User-Agent"),
                LocalDateTime.now(),
                ex
        );
    }

    /**
     * Client IP address helper
     * JavaScript req.ip karşılığı
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
