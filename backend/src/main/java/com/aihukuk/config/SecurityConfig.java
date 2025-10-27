// 🔒 Security Configuration - JavaScript security middleware'lerinin Spring karşılığı

package com.aihukuk.config;

import com.aihukuk.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

/**
 * Spring Security Configuration
 * 
 * JavaScript server.js'deki security middleware'lerin Spring Boot karşılığı:
 * - CORS (JavaScript cors middleware)
 * - Authentication (JavaScript authenticateToken middleware)  
 * - Password Encoding (JavaScript bcrypt)
 * - Rate Limiting (application.yml'da tanımlı)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Circular dependency'den kaçınmak için @Lazy kullanıyoruz
    @Autowired
    @org.springframework.context.annotation.Lazy
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // CORS Configuration (JavaScript CORS middleware karşılığı)
    @Value("${cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:Content-Type,Authorization,X-Requested-With}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    /**
     * Security Filter Chain
     * JavaScript security middleware'lerin Spring Security karşılığı
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF disable (REST API için) - JavaScript'te CSRF koruması yok
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS enable (JavaScript cors middleware karşılığı)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Session management (JWT kullandığımız için stateless)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Authorization rules (JavaScript route korumaları)
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (JavaScript'te middleware olmayan route'lar)
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/health/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/auth/refresh").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/auth/test").permitAll()
                
                // Swagger/API Docs (development)
                .requestMatchers("/swagger-ui/**", "/v1/api-docs/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                
                // Protected endpoints (JavaScript authenticateToken middleware)
                .requestMatchers("/v1/auth/profile", "/v1/auth/logout").authenticated()
                
                // Diğer tüm istekler authenticate olmalı
                .anyRequest().authenticated()
            )
            
            // JWT Filter ekle (JavaScript authenticateToken middleware)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS Configuration Source
     * JavaScript CORS middleware'inin Spring karşılığı
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // JavaScript corsOrigins configuration
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins.stream().map(String::trim).toList());
        
        // JavaScript allowedMethods
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        configuration.setAllowedMethods(methods.stream().map(String::trim).toList());
        
        // JavaScript allowedHeaders  
        List<String> headers = Arrays.asList(allowedHeaders.split(","));
        configuration.setAllowedHeaders(headers.stream().map(String::trim).toList());
        
        // JavaScript credentials: true
        configuration.setAllowCredentials(allowCredentials);
        
        // Max age (JavaScript'te yoktu, ekstra güvenlik)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * Password Encoder
     * JavaScript bcrypt karşılığı
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // JavaScript'teki bcrypt.hash ve bcrypt.compare karşılığı
        return new BCryptPasswordEncoder(12); // application.yml'daki bcrypt.rounds
    }

    /**
     * Authentication Manager
     * Spring Security authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * User Details Service
     * JWT authentication için kullanıcı yükleme servisi
     * Circular dependency'den kaçınmak için UserRepository'i direkt kullanıyoruz
     */
    @Bean
    public UserDetailsService userDetailsService(com.aihukuk.repository.UserRepository userRepository) {
        return email -> {
            var user = userRepository.findByEmailIgnoreCaseAndStatusNot(email, 
                com.aihukuk.entity.User.UserStatus.DELETED).orElse(null);
            if (user == null) {
                throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + email);
            }
            
            // Spring Security UserDetails oluştur
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPasswordHash())
                    .authorities("USER") // Temel kullanıcı rolü
                    .accountExpired(false)
                    .accountLocked(!user.isActive()) // JavaScript status kontrolü
                    .credentialsExpired(false)
                    .disabled(user.getStatus() == com.aihukuk.entity.User.UserStatus.DELETED)
                    .build();
        };
    }
}
