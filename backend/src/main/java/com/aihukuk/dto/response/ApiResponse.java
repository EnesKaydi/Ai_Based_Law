// 📋 API Response DTO - JavaScript response formatının karşılığı

package com.aihukuk.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * Standart API Response DTO
 * 
 * JavaScript'teki tüm endpoint response formatının karşılığı
 * Format: { success: boolean, message: string, data: object, code?: string }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String code;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    // Default Constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Success Constructor
    public ApiResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Full Constructor
    public ApiResponse(boolean success, String message, T data, String code) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    // Static Factory Methods (JavaScript response pattern'lerinin karşılığı)

    /**
     * Başarılı response oluştur
     * JavaScript: res.json({ success: true, message: "...", data: ... })
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Başarılı response oluştur (data olmadan)
     * JavaScript: res.json({ success: true, message: "..." })
     */
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    /**
     * Hata response oluştur
     * JavaScript: res.json({ success: false, message: "...", code: "..." })
     */
    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(false, message, null, code);
    }

    /**
     * Hata response oluştur (data ile birlikte)
     * JavaScript: res.json({ success: false, message: "...", code: "...", data: ... })
     */
    public static <T> ApiResponse<T> error(String message, String code, T data) {
        return new ApiResponse<>(false, message, data, code);
    }

    /**
     * Hata response oluştur (sadece mesaj)
     * JavaScript: res.json({ success: false, message: "..." })
     */
    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", code='" + code + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
