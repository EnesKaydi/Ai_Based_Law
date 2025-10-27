// 🏛️ AI Tabanlı Hukuk Sistemi - Spring Boot Application

package com.aihukuk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * AI Hukuk Sistemi Backend Application
 * 
 * JavaScript server.js dosyasının Java Spring Boot karşılığı
 * 
 * Özellikler:
 * - JWT tabanlı authentication
 * - MySQL veritabanı entegrasyonu
 * - RESTful API endpoints
 * - Security ve error handling
 * - Health check endpoints
 * 
 * @author AI Hukuk Sistemi Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing // Otomatik createdAt/updatedAt için
@ConfigurationPropertiesScan // Configuration properties'leri taramak için
public class AiHukukApplication {

    /**
     * Spring Boot uygulaması başlatıcı metod
     * JavaScript'te server.js'deki startServer() metodunun karşılığı
     * 
     * @param args Komut satırı argümanları
     */
    public static void main(String[] args) {
        // Spring Boot uygulamasını başlat
        SpringApplication app = new SpringApplication(AiHukukApplication.class);
        
        // Startup banner'ı özelleştir
        app.setBannerMode(org.springframework.boot.Banner.Mode.CONSOLE);
        
        // Uygulamayı çalıştır
        var context = app.run(args);
        
        // Başarılı başlatma mesajı (JavaScript server.js'deki gibi)
        System.out.println("""
            
            🏛️ AI Tabanlı Hukuk Sistemi Backend (Java Spring Boot)
            📡 Server is running successfully
            🌍 Environment: %s
            🗄️ Database: MySQL
            ⚖️ Hukuk ve Adalet için hazır!
            
            📋 Available Endpoints:
            🔗 Health Check: /v1/health
            🔐 Authentication: /v1/auth/*
            📖 API Docs: /swagger-ui/index.html
            
            """.formatted(getEnvironment(context))
        );
    }

    /**
     * Aktif profili al
     * 
     * @param context Spring application context
     * @return Aktif environment profili
     */
    private static String getEnvironment(org.springframework.context.ConfigurableApplicationContext context) {
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        return activeProfiles.length > 0 ? activeProfiles[0] : "default";
    }
}
