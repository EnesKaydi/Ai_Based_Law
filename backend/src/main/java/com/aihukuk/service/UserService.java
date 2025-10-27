// 👤 User Service - JavaScript User model metodlarının service karşılığı

package com.aihukuk.service;

import com.aihukuk.entity.User;
import com.aihukuk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * User Service Sınıfı
 * 
 * JavaScript User.js sınıfının metodlarının service layer karşılığı
 * 
 * İşlevler:
 * - createUser() -> JavaScript User.create()
 * - authenticateUser() -> JavaScript User.authenticate()
 * - updateLastLogin() -> JavaScript User.updateLastLogin()
 * - verifyEmail() -> JavaScript User.verifyEmail()
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private PasswordEncoder passwordEncoder;

    @Value("${bcrypt.rounds:12}")
    private int bcryptRounds;

    /**
     * Yeni kullanıcı oluştur
     * JavaScript User.create() metodunun karşılığı
     * 
     * @param fullName Ad soyad
     * @param email Email adresi
     * @param password Şifre (plain text)
     * @return Oluşturulan kullanıcı
     * @throws IllegalArgumentException Email zaten varsa
     */
    public User createUser(String fullName, String email, String password) {
        // Email kontrolü (JavaScript'teki existingUser kontrolü)
        if (userRepository.existsByEmailIgnoreCaseAndStatusNot(email, User.UserStatus.DELETED)) {
            throw new IllegalArgumentException("Bu email adresi zaten kullanılıyor");
        }

        // Şifreyi hash'le (JavaScript bcrypt.hash karşılığı)
        String hashedPassword = passwordEncoder.encode(password);

        // User oluştur
        User user = new User(fullName, email, hashedPassword);
        
        // Veritabanına kaydet
        return userRepository.save(user);
    }

    /**
     * Kullanıcı kimlik doğrulaması
     * JavaScript User.authenticate() metodunun karşılığı
     * 
     * @param email Email adresi
     * @param password Şifre (plain text)
     * @return Doğrulanmış kullanıcı veya null
     */
    public User authenticateUser(String email, String password) {
        // Aktif kullanıcıyı bul (JavaScript'teki status = "active" kontrolü)
        Optional<User> userOpt = userRepository.findByEmailIgnoreCaseAndStatus(email, User.UserStatus.ACTIVE);
        
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();

        // Şifre kontrolü (JavaScript bcrypt.compare karşılığı)
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return null;
        }

        // Son giriş zamanını güncelle (JavaScript User.updateLastLogin karşılığı)
        updateLastLogin(user.getId());
        user.updateLastLogin(); // Entity'deki helper metod

        return user;
    }

    /**
     * ID'ye göre kullanıcı bul
     * JavaScript User.findById() metodunun karşılığı
     * 
     * @param id Kullanıcı ID'si
     * @return Kullanıcı veya null
     */
    public User findById(Long id) {
        return userRepository.findByIdAndStatusNot(id, User.UserStatus.DELETED).orElse(null);
    }

    /**
     * Email'e göre kullanıcı bul
     * JavaScript User.findByEmail() metodunun karşılığı
     * 
     * @param email Email adresi
     * @return Kullanıcı veya null
     */
    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCaseAndStatusNot(email, User.UserStatus.DELETED).orElse(null);
    }

    /**
     * UUID'ye göre kullanıcı bul
     * JavaScript User.findByUuid() metodunun karşılığı
     * 
     * @param uuid UUID string
     * @return Kullanıcı veya null
     */
    public User findByUuid(String uuid) {
        return userRepository.findByUuidAndStatusNot(uuid, User.UserStatus.DELETED).orElse(null);
    }

    /**
     * Son giriş zamanını güncelle
     * JavaScript User.updateLastLogin() metodunun karşılığı
     * 
     * @param userId Kullanıcı ID'si
     */
    public void updateLastLogin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    /**
     * Email doğrulamasını işaretle
     * JavaScript User.verifyEmail() metodunun karşılığı
     * 
     * @param userId Kullanıcı ID'si
     */
    public void verifyEmail(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.markEmailAsVerified();
            userRepository.save(user);
        }
    }

    /**
     * Kullanıcı şifresi güncelle
     * JavaScript User.updatePassword() metodunun karşılığı
     * 
     * @param userId Kullanıcı ID'si
     * @param newPassword Yeni şifre (plain text)
     */
    public void updatePassword(Long userId, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(hashedPassword);
            userRepository.save(user);
        }
    }

    /**
     * Kullanıcı istatistikleri al
     * JavaScript User.getStats() metodunun karşılığı
     * 
     * @return Kullanıcı istatistikleri
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getUserStats() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        User.UserStatus deletedStatus = User.UserStatus.DELETED;

        long total = userRepository.countByStatusNot(deletedStatus);
        long active = userRepository.countByStatus(User.UserStatus.ACTIVE);
        long verified = userRepository.countVerifiedUsers(deletedStatus);
        long recent = userRepository.countRecentUsers(thirtyDaysAgo, deletedStatus);
        long pending = userRepository.countPendingVerificationUsers(User.UserStatus.ACTIVE);

        return Map.of(
            "total", total,
            "active", active,
            "verified", verified,
            "recent", recent,
            "pendingVerification", pending
        );
    }

    /**
     * Email adresi zaten var mı kontrol et
     * 
     * @param email Email adresi
     * @return Email varsa true
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmailIgnoreCaseAndStatusNot(email, User.UserStatus.DELETED);
    }

    /**
     * Aktif kullanıcıları say
     * 
     * @return Aktif kullanıcı sayısı
     */
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countByStatus(User.UserStatus.ACTIVE);
    }
}
