# 🚀 AI Tabanlı Hukuk Sistemi - Backend API Dokümantasyonu

## 📋 İçindekiler
- [Genel Bilgiler](#genel-bilgiler)
- [Kimlik Doğrulama API'leri](#kimlik-doğrulama-apileri)
- [Veritabanı Modelleri](#veritabanı-modelleri)
- [Hata Kodları](#hata-kodları)
- [Güvenlik](#güvenlik)

---

## 🔧 Genel Bilgiler

### Base URL
```
Production:  https://api.ai-hukuk.com/v1
Development: http://localhost:8000/v1
```

### Content-Type
```
Content-Type: application/json
Accept: application/json
```

### Authentication
```
Authorization: Bearer <JWT_TOKEN>
```

---

## 🔐 Kimlik Doğrulama API'leri

### 1. Kullanıcı Kayıt

**Endpoint:** `POST /auth/register`

**Açıklama:** Yeni kullanıcı kaydı oluşturur ve email doğrulama kodu gönderir.

**Request Body:**
```json
{
  "fullName": "Ahmet Yılmaz",
  "email": "ahmet@example.com",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "termsAccepted": true
}
```

**Validation Rules:**
- `fullName`: Minimum 2, maksimum 100 karakter
- `email`: Geçerli email formatı, benzersiz olmak zorunda
- `password`: Minimum 8 karakter, en az 1 büyük harf, 1 küçük harf, 1 rakam, 1 özel karakter
- `confirmPassword`: password ile eşleşmek zorunda
- `termsAccepted`: true olmak zorunda

**Success Response (201):**
```json
{
  "success": true,
  "message": "Kullanıcı başarıyla oluşturuldu. Lütfen email'inizi doğrulayın.",
  "data": {
    "userId": "uuid-123-456-789",
    "email": "ahmet@example.com",
    "emailVerificationSent": true,
    "verificationExpiresAt": "2024-01-15T10:30:00Z"
  }
}
```

**Error Responses:**
```json
// 400 - Validation Error
{
  "success": false,
  "message": "Doğrulama hatası",
  "errors": {
    "email": ["Bu email adresi zaten kullanılıyor"],
    "password": ["Şifre en az 8 karakter olmalıdır"]
  }
}

// 429 - Rate Limit
{
  "success": false,
  "message": "Çok fazla kayıt denemesi. 15 dakika sonra tekrar deneyin."
}
```

---

### 2. Email Doğrulama

**Endpoint:** `POST /auth/verify-email`

**Açıklama:** Email adresini doğrulama kodu ile onaylar.

**Request Body:**
```json
{
  "email": "ahmet@example.com",
  "verificationCode": "123456"
}
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Email adresi başarıyla doğrulandı.",
  "data": {
    "userId": "uuid-123-456-789",
    "emailVerified": true,
    "verifiedAt": "2024-01-15T10:25:00Z"
  }
}
```

**Error Responses:**
```json
// 400 - Geçersiz kod
{
  "success": false,
  "message": "Doğrulama kodu geçersiz veya süresi dolmuş."
}

// 404 - Kullanıcı bulunamadı
{
  "success": false,
  "message": "Kullanıcı bulunamadı."
}
```

---

### 3. Doğrulama Kodu Yeniden Gönder

**Endpoint:** `POST /auth/resend-verification`

**Açıklama:** Yeni email doğrulama kodu gönderir.

**Request Body:**
```json
{
  "email": "ahmet@example.com"
}
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Yeni doğrulama kodu gönderildi.",
  "data": {
    "verificationSent": true,
    "expiresAt": "2024-01-15T10:45:00Z"
  }
}
```

---

### 4. Kullanıcı Girişi

**Endpoint:** `POST /auth/login`

**Açıklama:** Email ve şifre ile kullanıcı girişi yapar.

**Request Body:**
```json
{
  "email": "ahmet@example.com",
  "password": "SecurePass123!",
  "rememberMe": false
}
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Giriş başarılı.",
  "data": {
    "user": {
      "id": "uuid-123-456-789",
      "fullName": "Ahmet Yılmaz",
      "email": "ahmet@example.com",
      "emailVerified": true,
      "createdAt": "2024-01-15T09:00:00Z"
    },
    "tokens": {
      "accessToken": "jwt-access-token-here",
      "refreshToken": "jwt-refresh-token-here",
      "expiresIn": 3600,
      "tokenType": "Bearer"
    }
  }
}
```

**Error Responses:**
```json
// 401 - Geçersiz bilgiler
{
  "success": false,
  "message": "Email veya şifre hatalı."
}

// 403 - Email doğrulanmamış
{
  "success": false,
  "message": "Lütfen önce email adresinizi doğrulayın.",
  "data": {
    "emailVerificationRequired": true
  }
}

// 429 - Rate Limit
{
  "success": false,
  "message": "Çok fazla giriş denemesi. 15 dakika sonra tekrar deneyin."
}
```

---

### 5. Token Yenileme

**Endpoint:** `POST /auth/refresh`

**Açıklama:** Refresh token ile yeni access token alır.

**Request Body:**
```json
{
  "refreshToken": "jwt-refresh-token-here"
}
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Token yenilendi.",
  "data": {
    "accessToken": "new-jwt-access-token",
    "expiresIn": 3600,
    "tokenType": "Bearer"
  }
}
```

---

### 6. Şifre Sıfırlama İsteği

**Endpoint:** `POST /auth/forgot-password`

**Açıklama:** Şifre sıfırlama kodu gönderir.

**Request Body:**
```json
{
  "email": "ahmet@example.com"
}
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Şifre sıfırlama kodu email adresinize gönderildi.",
  "data": {
    "resetCodeSent": true,
    "expiresAt": "2024-01-15T11:00:00Z"
  }
}
```

---

### 7. Şifre Sıfırlama

**Endpoint:** `POST /auth/reset-password`

**Açıklama:** Sıfırlama kodu ile yeni şifre belirler.

**Request Body:**
```json
{
  "email": "ahmet@example.com",
  "resetCode": "654321",
  "newPassword": "NewSecurePass123!",
  "confirmPassword": "NewSecurePass123!"
}
```

**Success Response (200):**
```json
{
  "success": true,
  "message": "Şifre başarıyla güncellendi.",
  "data": {
    "passwordUpdated": true,
    "updatedAt": "2024-01-15T10:30:00Z"
  }
}
```

---

### 8. Çıkış

**Endpoint:** `POST /auth/logout`

**Headers:** `Authorization: Bearer <access_token>`

**Success Response (200):**
```json
{
  "success": true,
  "message": "Başarıyla çıkış yapıldı."
}
```

---

## 🗄️ Veritabanı Modelleri

### Users Tablosu
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE,
    email_verified_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    status ENUM('active', 'suspended', 'deleted') DEFAULT 'active'
);
```

### Email Verifications Tablosu
```sql
CREATE TABLE email_verifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    verification_code VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    verified_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attempts INT DEFAULT 0
);
```

### Password Resets Tablosu
```sql
CREATE TABLE password_resets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    reset_code VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attempts INT DEFAULT 0
);
```

### User Sessions Tablosu
```sql
CREATE TABLE user_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    refresh_token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address INET,
    user_agent TEXT,
    revoked BOOLEAN DEFAULT FALSE
);
```

---

## ⚠️ Hata Kodları

### HTTP Status Codes
- `200` - OK: İstek başarılı
- `201` - Created: Kaynak başarıyla oluşturuldu
- `400` - Bad Request: İstek formatı hatalı
- `401` - Unauthorized: Kimlik doğrulama gerekli
- `403` - Forbidden: Erişim izni yok
- `404` - Not Found: Kaynak bulunamadı
- `409` - Conflict: Veri çakışması (duplicate email vb.)
- `422` - Unprocessable Entity: Doğrulama hatası
- `429` - Too Many Requests: Rate limit aşıldı
- `500` - Internal Server Error: Sunucu hatası

### Application Error Codes
```json
{
  "AUTH_001": "Email adresi geçersiz",
  "AUTH_002": "Şifre güvenlik kriterlerini karşılamıyor",
  "AUTH_003": "Email adresi zaten kullanılıyor",
  "AUTH_004": "Doğrulama kodu geçersiz",
  "AUTH_005": "Doğrulama kodu süresi dolmuş",
  "AUTH_006": "Çok fazla doğrulama denemesi",
  "AUTH_007": "Email doğrulanmamış",
  "AUTH_008": "Geçersiz kimlik bilgileri",
  "AUTH_009": "Hesap askıya alınmış",
  "AUTH_010": "Token geçersiz veya süresi dolmuş"
}
```

---

## 🔒 Güvenlik

### Rate Limiting
- **Kayıt:** 3 deneme / 15 dakika / IP
- **Giriş:** 5 deneme / 15 dakika / IP  
- **Email doğrulama:** 5 deneme / 15 dakika / email
- **Şifre sıfırlama:** 3 deneme / 60 dakika / email

### Password Security
- Minimum 8 karakter
- En az 1 büyük harf
- En az 1 küçük harf  
- En az 1 rakam
- En az 1 özel karakter (!@#$%^&*)
- Bcrypt hash (12 rounds)

### JWT Configuration
- **Access Token:** 1 saat
- **Refresh Token:** 30 gün
- **Algorithm:** RS256
- **Issuer:** ai-hukuk-api
- **Audience:** ai-hukuk-frontend

### Email Security
- **SMTP TLS/SSL:** Zorunlu
- **Doğrulama kodu:** 6 haneli, 15 dakika geçerli
- **Rate limiting per email:** 5 istek / saat

### CORS Policy
```json
{
  "origins": [
    "https://ai-hukuk.com",
    "https://www.ai-hukuk.com",
    "http://localhost:3000"
  ],
  "methods": ["GET", "POST", "PUT", "DELETE", "OPTIONS"],
  "allowedHeaders": ["Content-Type", "Authorization"],
  "credentials": true
}
```

---

## 📧 Email Templates

### Doğrulama Email'i
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>AI Hukuk Sistemi - Email Doğrulama</title>
</head>
<body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
    <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px;">
        <div style="text-align: center; margin-bottom: 30px;">
            <h1 style="color: #1e40af;">⚖️ AI Tabanlı Hukuk Sistemi</h1>
        </div>
        
        <h2 style="color: #333;">Email Adresinizi Doğrulayın</h2>
        
        <p>Merhaba <strong>{{fullName}}</strong>,</p>
        
        <p>AI Tabanlı Hukuk Sistemi'ne hoş geldiniz! Hesabınızı aktifleştirmek için aşağıdaki doğrulama kodunu kullanın:</p>
        
        <div style="text-align: center; margin: 30px 0;">
            <div style="background-color: #eff6ff; border: 2px solid #3b82f6; border-radius: 8px; padding: 20px; display: inline-block;">
                <h1 style="margin: 0; color: #1e40af; font-size: 36px; letter-spacing: 8px;">{{verificationCode}}</h1>
            </div>
        </div>
        
        <p style="color: #666;">Bu kod <strong>15 dakika</strong> süreyle geçerlidir.</p>
        
        <p style="color: #666; font-size: 14px;">
            Eğer bu işlemi siz yapmadıysanız, bu email'i görmezden gelebilirsiniz.
        </p>
        
        <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
        
        <p style="color: #999; font-size: 12px; text-align: center;">
            © 2024 AI Tabanlı Hukuk Sistemi. Tüm hakları saklıdır.
        </p>
    </div>
</body>
</html>
```

---

## 🧪 Test Senaryoları

### Kayıt İşlemi Test Case'leri
1. **Başarılı kayıt**
2. **Duplicate email kontrolü**
3. **Zayıf şifre kontrolü**
4. **Email format kontrolü**
5. **Rate limiting testi**

### Giriş İşlemi Test Case'leri
1. **Başarılı giriş**
2. **Hatalı şifre**
3. **Doğrulanmamış email**
4. **Rate limiting testi**
5. **Token üretimi kontrolü**

### Email Doğrulama Test Case'leri
1. **Başarılı doğrulama**
2. **Geçersiz kod**
3. **Süresi dolmuş kod**
4. **Çoklu deneme limiti**

---

## 📊 Monitoring ve Logging

### Log Levels
- **ERROR:** Sistem hataları
- **WARN:** Rate limit, güvenlik uyarıları
- **INFO:** Kullanıcı işlemleri
- **DEBUG:** Detaylı bilgiler

### Metrics
- Kayıt sayısı (günlük/haftalık)
- Giriş sayısı (günlük/haftalık)
- Email doğrulama oranı
- API response time
- Error rate

---

*Bu doküman AI Tabanlı Hukuk Sistemi Backend API v1.0 için hazırlanmıştır.*
*Son güncelleme: {{ current_date }}*
