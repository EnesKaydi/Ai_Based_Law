// 🗄️ User Repository - JavaScript User.js static metodlarının JPA karşılığı

package com.aihukuk.repository;

import com.aihukuk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * User Repository Interface
 * 
 * JavaScript User.js sınıfındaki static metodların Spring Data JPA karşılığı
 * 
 * Metodlar:
 * - findByEmail() -> JavaScript User.findByEmail()
 * - findByUuid() -> JavaScript User.findByUuid()
 * - findActiveById() -> JavaScript User.findById() + status kontrolü
 * - existsByEmail() -> Email varlık kontrolü
 * - getUserStats() -> JavaScript User.getStats()
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Email adresine göre kullanıcı bul
     * JavaScript User.findByEmail() metodunun karşılığı
     * 
     * @param email Email adresi (büyük/küçük harf duyarlı değil)
     * @return Kullanıcı varsa Optional<User>, yoksa empty
     */
    Optional<User> findByEmailIgnoreCaseAndStatusNot(String email, User.UserStatus status);

    /**
     * Email ile aktif kullanıcı bul (giriş için)
     * JavaScript User.authenticate() içindeki sorgunun karşılığı
     * 
     * @param email Email adresi
     * @return Aktif kullanıcı varsa Optional<User>
     */
    Optional<User> findByEmailIgnoreCaseAndStatus(String email, User.UserStatus status);

    /**
     * UUID'ye göre aktif kullanıcı bul
     * JavaScript User.findByUuid() metodunun karşılığı
     * 
     * @param uuid Kullanıcı UUID'si
     * @return Silinmemiş kullanıcı varsa Optional<User>
     */
    Optional<User> findByUuidAndStatusNot(String uuid, User.UserStatus status);

    /**
     * ID'ye göre aktif kullanıcı bul
     * JavaScript User.findById() metodunun karşılığı
     * 
     * @param id Kullanıcı ID'si
     * @return Silinmemiş kullanıcı varsa Optional<User>
     */
    Optional<User> findByIdAndStatusNot(Long id, User.UserStatus status);

    /**
     * Email adresi zaten var mı kontrol et
     * JavaScript User.create() içindeki dublicate kontrolünün karşılığı
     * 
     * @param email Email adresi
     * @return Email varsa true, yoksa false
     */
    boolean existsByEmailIgnoreCaseAndStatusNot(String email, User.UserStatus status);

    /**
     * Toplam kullanıcı sayısı (silinmemiş)
     * JavaScript User.getStats() metodunun bir parçası
     * 
     * @return Silinmemiş kullanıcı sayısı
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.status != :deletedStatus")
    long countByStatusNot(@Param("deletedStatus") User.UserStatus deletedStatus);

    /**
     * Aktif kullanıcı sayısı
     * JavaScript User.getStats() metodunun bir parçası
     * 
     * @return Aktif kullanıcı sayısı
     */
    long countByStatus(User.UserStatus status);

    /**
     * Email doğrulanmış kullanıcı sayısı
     * JavaScript User.getStats() metodunun bir parçası
     * 
     * @return Email doğrulanmış kullanıcı sayısı
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = true AND u.status != :deletedStatus")
    long countVerifiedUsers(@Param("deletedStatus") User.UserStatus deletedStatus);

    /**
     * Son 30 günde kayıt olan kullanıcı sayısı
     * JavaScript User.getStats() metodunun bir parçası
     * 
     * @param startDate 30 gün önceki tarih
     * @return Yeni kullanıcı sayısı
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt > :startDate AND u.status != :deletedStatus")
    long countRecentUsers(@Param("startDate") LocalDateTime startDate, @Param("deletedStatus") User.UserStatus deletedStatus);

    /**
     * Kullanıcı istatistikleri (tek sorguda)
     * JavaScript User.getStats() metodunun optimize edilmiş karşılığı
     */
    @Query("""
        SELECT new map(
            COUNT(u) as total,
            SUM(CASE WHEN u.status = 'ACTIVE' THEN 1 ELSE 0 END) as active,
            SUM(CASE WHEN u.emailVerified = true THEN 1 ELSE 0 END) as verified,
            SUM(CASE WHEN u.createdAt > :startDate THEN 1 ELSE 0 END) as recent
        )
        FROM User u 
        WHERE u.status != :deletedStatus
    """)
    Object getUserStatistics(@Param("startDate") LocalDateTime startDate, @Param("deletedStatus") User.UserStatus deletedStatus);

    /**
     * Son giriş yapan kullanıcılar
     * Ek özellik (JavaScript'te yoktu)
     * 
     * @param limit Kaç kullanıcı getirileceği
     * @return Son giriş yapan kullanıcılar
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt IS NOT NULL AND u.status != :deletedStatus ORDER BY u.lastLoginAt DESC")
    java.util.List<User> findRecentlyActiveUsers(@Param("deletedStatus") User.UserStatus deletedStatus, org.springframework.data.domain.Pageable pageable);

    /**
     * Email doğrulaması bekleyen kullanıcılar
     * Ek özellik (JavaScript'te yoktu)
     * 
     * @return Doğrulama bekleyen kullanıcı sayısı
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.emailVerified = false AND u.status = :activeStatus")
    long countPendingVerificationUsers(@Param("activeStatus") User.UserStatus activeStatus);
}
