// 🏥 Health Controller - JavaScript health.js routes'ının Spring karşılığı

package com.aihukuk.controller;

import com.aihukuk.dto.response.ApiResponse;
import com.aihukuk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * 
 * JavaScript health.js router'ının Spring Boot @RestController karşılığı
 * 
 * Endpoint'ler:
 * - GET /v1/health -> JavaScript router.get('/')
 * - GET /v1/health/status -> JavaScript router.get('/status') 
 * - GET /v1/health/db -> JavaScript router.get('/db')
 */
@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    /**
     * Temel health check endpoint'i
     * JavaScript router.get('/') metodunun karşılığı
     * 
     * @return Sistem sağlık durumu
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        try {
            // JavaScript healthStatus object'inin karşılığı
            Map<String, Object> healthStatus = new HashMap<>();
            healthStatus.put("status", "healthy");
            healthStatus.put("timestamp", LocalDateTime.now());
            healthStatus.put("uptime", getUptime());
            healthStatus.put("version", "1.0.0");
            healthStatus.put("java", System.getProperty("java.version"));
            healthStatus.put("environment", getActiveProfile());

            // Memory bilgileri (JavaScript memory object'i)
            Map<String, Object> memory = new HashMap<>();
            Runtime runtime = Runtime.getRuntime();
            memory.put("used", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024); // MB
            memory.put("total", runtime.totalMemory() / 1024 / 1024); // MB
            memory.put("max", runtime.maxMemory() / 1024 / 1024); // MB
            memory.put("free", runtime.freeMemory() / 1024 / 1024); // MB
            healthStatus.put("memory", memory);

            // Database durumu (JavaScript database check)
            Map<String, Object> database = new HashMap<>();
            boolean dbConnected = testDatabaseConnection();
            database.put("connected", dbConnected);
            
            if (dbConnected) {
                database.put("stats", getDatabaseStats());
            } else {
                database.put("stats", null);
            }
            healthStatus.put("database", database);

            // JavaScript: const httpStatus = dbConnected ? 200 : 503;
            HttpStatus httpStatus = dbConnected ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
            
            if (!dbConnected) {
                healthStatus.put("status", "unhealthy");
            }

            // JavaScript: res.status(httpStatus).json({ success: true, data: healthStatus })
            return ResponseEntity.status(httpStatus)
                    .body(ApiResponse.success("Health check completed", healthStatus));

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            Map<String, Object> errorStatus = Map.of(
                "status", "unhealthy",
                "timestamp", LocalDateTime.now(),
                "error", e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("Health check failed", "HEALTH_CHECK_ERROR", errorStatus));
        }
    }

    /**
     * Detaylı sistem durumu endpoint'i
     * JavaScript router.get('/status') metodunun karşılığı
     * 
     * @return Detaylı sistem bilgileri
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatus() {
        try {
            Map<String, Object> status = new HashMap<>();

            // Server bilgileri (JavaScript server object'i)
            Map<String, Object> server = new HashMap<>();
            server.put("status", "running");
            server.put("uptime", getUptime());
            server.put("environment", getActiveProfile());
            server.put("pid", getProcessId());
            server.put("platform", System.getProperty("os.name"));
            server.put("arch", System.getProperty("os.arch"));
            server.put("javaVersion", System.getProperty("java.version"));
            status.put("server", server);

            // Database durumu
            Map<String, Object> database = new HashMap<>();
            boolean dbConnected = testDatabaseConnection();
            database.put("connected", dbConnected);
            
            if (dbConnected) {
                database.put("stats", getDatabaseStats());
            }
            status.put("database", database);

            // Memory bilgileri (JavaScript memory object'inin detaylısı)
            Runtime runtime = Runtime.getRuntime();
            Map<String, Object> memory = new HashMap<>();
            memory.put("heapTotal", runtime.totalMemory());
            memory.put("heapUsed", runtime.totalMemory() - runtime.freeMemory());
            memory.put("heapFree", runtime.freeMemory());
            memory.put("heapMax", runtime.maxMemory());
            status.put("memory", memory);

            // User istatistikleri (JavaScript users object'i)
            Map<String, Object> users = null;
            if (dbConnected) {
                users = userService.getUserStats();
            }
            status.put("users", users);

            // JavaScript: res.json({ success: true, data: status })
            return ResponseEntity.ok(ApiResponse.success("Status retrieved", status));

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Status check failed", "STATUS_ERROR"));
        }
    }

    /**
     * Database connectivity test endpoint'i
     * JavaScript router.get('/db') metodunun karşılığı
     * 
     * @return Database bağlantı durumu
     */
    @GetMapping("/db")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDatabaseHealth() {
        try {
            boolean connected = testDatabaseConnection();
            Map<String, Object> stats = connected ? getDatabaseStats() : null;

            Map<String, Object> dbHealth = Map.of(
                "connected", connected,
                "stats", stats,
                "timestamp", LocalDateTime.now()
            );

            // JavaScript: res.json({ success: true, data: { connected, stats, timestamp } })
            return ResponseEntity.ok(ApiResponse.success("Database health checked", dbHealth));

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Database health check failed", "DB_HEALTH_ERROR"));
        }
    }

    // Helper Methods

    /**
     * Database bağlantı testi
     * JavaScript testConnection() metodunun karşılığı
     */
    private boolean testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Database istatistikleri
     * JavaScript getDatabaseStats() metodunun karşılığı
     */
    private Map<String, Object> getDatabaseStats() {
        try {
            // Bu bilgiler HikariCP connection pool'dan alınabilir
            Map<String, Object> stats = new HashMap<>();
            stats.put("connectionPoolActive", "N/A"); // HikariCP integration gerekli
            stats.put("connectionPoolIdle", "N/A");
            stats.put("connectionPoolTotal", "N/A");
            stats.put("userCount", userService.countActiveUsers());
            
            return stats;
        } catch (Exception e) {
            return Map.of("error", "Stats unavailable");
        }
    }

    /**
     * Uptime hesapla
     * JavaScript process.uptime() karşılığı
     */
    private long getUptime() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime() / 1000; // Saniye
    }

    /**
     * Aktif profil
     * JavaScript process.env.NODE_ENV karşılığı
     */
    private String getActiveProfile() {
        return System.getProperty("spring.profiles.active", "default");
    }

    /**
     * Process ID
     * JavaScript process.pid karşılığı
     */
    private String getProcessId() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
