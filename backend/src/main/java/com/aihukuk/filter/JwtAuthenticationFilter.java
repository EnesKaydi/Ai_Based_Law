// 🔐 JWT Authentication Filter - JavaScript authenticateToken middleware'inin karşılığı

package com.aihukuk.filter;

import com.aihukuk.entity.User;
import com.aihukuk.service.UserService;
import com.aihukuk.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Authentication Filter
 * 
 * JavaScript middleware/auth.js authenticateToken fonksiyonunun Spring Security karşılığı
 * 
 * İşlevler:
 * - Authorization header'ından token çıkar
 * - Token'ı doğrula
 * - Kullanıcıyı bul ve authenticate et
 * - Security context'e kullanıcıyı set et
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JWT Authentication Filter
     * JavaScript authenticateToken middleware'inin doOneByOne karşılığı
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Authorization header'ını al (JavaScript req.headers['authorization'])
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String email = null;

            // Token'ı çıkar (JavaScript authHeader && authHeader.split(' ')[1])
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // "Bearer " kısmını at
                
                try {
                    // Token'dan email çıkar (JavaScript jwt.verify(token, process.env.JWT_SECRET))
                    email = jwtUtil.extractEmail(token);
                } catch (Exception e) {
                    // Token geçersizse hata response gönder
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 
                                    "Geçersiz token", "AUTH_004");
                    return;
                }
            }

            // Email varsa ve henüz authenticate olmamışsa
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Kullanıcıyı veritabanından al (JavaScript User.findById(decoded.userId))
                User user = userService.findByEmail(email);
                
                if (user == null) {
                    // JavaScript: return res.status(401).json({ message: 'Geçersiz token - kullanici bulunamadi' })
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                                    "Geçersiz token - kullanici bulunamadi", "AUTH_002");
                    return;
                }

                // Kullanıcı aktif mi kontrol et (JavaScript user.status !== 'active')
                if (!user.isActive()) {
                    // JavaScript: return res.status(403).json({ message: 'Hesap askiya alinmis veya deaktif' })
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                                    "Hesap askiya alinmis veya deaktif", "AUTH_003");
                    return;
                }

                // UserDetails oluştur
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                
                // Token geçerli mi kontrol et (JavaScript jwt.verify validation)
                if (jwtUtil.validateAccessToken(token, userDetails)) {
                    // Authentication token oluştur (JavaScript req.user = user)
                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, 
                                    null, 
                                    userDetails.getAuthorities()
                            );
                    
                    // Request details set et
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Security context'e set et
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    // Request attribute'una user bilgisini ekle (JavaScript req.user)
                    request.setAttribute("currentUser", user);
                }
            }

            // Filter chain'i devam ettir (JavaScript next())
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // JavaScript catch block'un karşılığı
            logger.error("JWT Authentication error: " + e.getMessage(), e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Token dogrulama hatasi", "AUTH_006");
        }
    }

    /**
     * Hata response'u gönder
     * JavaScript error response format'ının karşılığı
     * 
     * @param response HTTP response
     * @param status HTTP status code
     * @param message Hata mesajı
     * @param code Hata kodu
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message, String code) 
            throws IOException {
        
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JavaScript error response format'ı
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("code", code);
        errorResponse.put("timestamp", java.time.LocalDateTime.now());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

    /**
     * Belirli path'lar için filter'ı atla
     * Public endpoint'ler için filter uygulanmaz
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // Public endpoint'ler (JavaScript'te middleware uygulanmayan route'lar)
        return path.startsWith("/v1/health") ||
               path.startsWith("/v1/auth/register") ||
               path.startsWith("/v1/auth/login") ||
               path.startsWith("/v1/auth/refresh") ||
               path.startsWith("/v1/auth/test") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v1/api-docs") ||
               path.equals("/") ||
               path.startsWith("/actuator");
    }
}
