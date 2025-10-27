# 🏛️ AI Tabanlı Hukuk Sistemi

> **Modern web teknolojileri ile geliştirilmiş kapsamlı hukuk destek platformu**

[![Next.js](https://img.shields.io/badge/Next.js-15.3.2-black?style=flat-square&logo=next.js)](https://nextjs.org/)
[![React](https://img.shields.io/badge/React-19-blue?style=flat-square&logo=react)](https://reactjs.org/)
[![Node.js](https://img.shields.io/badge/Node.js-20-green?style=flat-square&logo=node.js)](https://nodejs.org/)
[![Express](https://img.shields.io/badge/Express-4.x-lightgrey?style=flat-square&logo=express)](https://expressjs.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?style=flat-square&logo=mysql)](https://mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)](https://docker.com/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-blue?style=flat-square&logo=typescript)](https://typescriptlang.org/)

## 📋 İçindekiler

- [🎯 Proje Hakkında](#-proje-hakkında)
- [🛠️ Teknoloji Stack](#️-teknoloji-stack)
- [📁 Proje Yapısı](#-proje-yapısı)
- [🚀 Kurulum ve Çalıştırma](#-kurulum-ve-çalıştırma)
- [🐳 Docker ile Çalıştırma](#-docker-ile-çalıştırma)
- [📡 API Dokümantasyonu](#-api-dokümantasyonu)
- [🎨 Frontend Özellikleri](#-frontend-özellikleri)
- [⚙️ Backend Özellikleri](#️-backend-özellikleri)
- [📝 Geliştirme Süreci](#-geliştirme-süreci)
- [🔮 Gelecek Planları](#-gelecek-planları)

## 🎯 Proje Hakkında

AI Tabanlı Hukuk Sistemi, hukuki süreçleri dijitalleştiren ve kullanıcıların hukuki ihtiyaçlarını karşılamaya yönelik modern bir web platformudur.

### ✨ Temel Özellikler

- 🔐 **Güvenli Kimlik Doğrulama**: JWT tabanlı authentication sistemi
- 📱 **Responsive Tasarım**: Tüm cihazlarda uyumlu çalışır
- 🤖 **AI Hukuk Asistanı**: Yapay zeka destekli hukuki danışmanlık
- 📜 **Ana Yasa İndir**: Türk hukuk mevzuatına kolay erişim
- ⚖️ **Ceza Tahmini**: AI destekli ceza tahmin modülü
- 🏛️ **Toplum Hizmeti**: Toplum hizmeti bilgi sistemi

## 🛠️ Teknoloji Stack

### Frontend
- **Next.js 15.3.2** - React framework
- **React 19** - UI kütüphanesi
- **TypeScript 5** - Type safety
- **TailwindCSS 3.4** - Utility-first CSS framework
- **Glassmorphism Design** - Modern UI tasarım

### Backend
- **Java 17** - Runtime environment
- **Spring Boot 3.1.5** - Enterprise framework
- **Spring Security** - Security framework
- **Spring Data JPA** - Database ORM
- **MySQL 8.0** - İlişkisel veritabanı
- **JWT** - Authentication tokens
- **bcrypt** - Şifre hashleme
- **Swagger/OpenAPI** - API dokümantasyonu

### DevOps & Tools
- **Docker & Docker Compose** - Container orchestration
- **ESLint** - Code linting
- **Prometheus** - Monitoring (opsiyonel)
- **Git** - Version control
- **GitHub** - Repository hosting

## 📁 Proje Yapısı

```
Ai_Based_Law/
├── 📁 backend/                     # Backend API servisi (Java Spring Boot)
│   ├── 📁 src/main/java/com/aihukuk/
│   │   ├── 📁 config/
│   │   │   └── SecurityConfig.java  # Spring Security konfigürasyonu
│   │   ├── 📁 controller/
│   │   │   ├── AuthController.java  # Authentication endpoints
│   │   │   └── HealthController.java # Health check endpoints
│   │   ├── 📁 entity/
│   │   │   └── User.java            # JPA Entity
│   │   ├── 📁 filter/
│   │   │   └── JwtAuthenticationFilter.java # JWT filter
│   │   ├── 📁 service/
│   │   │   └── UserService.java     # Business logic
│   │   └── 📁 util/
│   │       └── JwtUtil.java          # JWT utilities
│   ├── src/main/resources/
│   │   └── application.yml          # Spring Boot konfigürasyonu
│   ├── Dockerfile                   # Backend container
│   └── pom.xml                      # Maven dependencies
│
├── 📁 frontend/                     # Next.js frontend
│   ├── 📁 src/
│   │   ├── 📁 app/
│   │   │   ├── 📁 ana-yasa/         # Ana Yasa modülü
│   │   │   ├── 📁 asistan/          # AI Asistan modülü
│   │   │   │   ├── 📁 ceza-tahmini/ # Ceza tahmini sayfası
│   │   │   │   └── 📁 toplum-hizmeti/ # Toplum hizmeti sayfası
│   │   │   ├── 📁 home/             # Ana dashboard
│   │   │   ├── 📁 api/health/       # Health check API
│   │   │   ├── globals.css          # Global stiller
│   │   │   ├── layout.tsx           # Ana layout
│   │   │   ├── page.tsx             # Login sayfası (giriş)
│   │   │   └── register/page.tsx    # Kayıt sayfası
│   │   └── 📁 utils/
│   │       ├── api.js               # API client
│   │       └── auth.js              # Auth utilities
│   ├── 📁 public/                   # Statik dosyalar
│   │   └── justice-motif.svg        # Hukuk temalı logo
│   ├── Dockerfile                   # Production container
│   ├── Dockerfile.dev               # Development container
│   └── next.config.ts               # Next.js konfigürasyon
│
├── 📁 monitoring/                   # İzleme konfigürasyonları
│   └── prometheus.yml               # Prometheus config
│
├── docker-compose.yml               # Production Docker Compose
├── docker-compose.dev.yml           # Development Docker Compose
├── deploy.sh                        # Deployment script
├── BACKEND_API_DOCUMENTATION.md     # API dokümantasyonu
├── DOCKER_SETUP.md                  # Docker kurulum rehberi
├── YapilacaklarListesi.md           # Proje roadmap
└── README.md                        # Bu dosya
```

## 🚀 Kurulum ve Çalıştırma

### Ön Gereksinimler
- Node.js 20+
- MySQL 8.0
- Docker ve Docker Compose (opsiyonel)
- Git

### 1. Repository'yi Klonlayın
```bash
git clone https://github.com/EnesKaydi/Ai_Based_Law.git
cd Ai_Based_Law
```

### 2. Manuel Kurulum

#### Backend Kurulumu
```bash
cd backend
npm install
```

#### Frontend Kurulumu
```bash
cd frontend
npm install
```

#### Veritabanı Kurulumu
1. MySQL 8.0 kurun ve çalıştırın
2. Veritabanı oluşturun:
```sql
CREATE DATABASE ai_hukuk_db;
CREATE USER 'ai_hukuk_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON ai_hukuk_db.* TO 'ai_hukuk_user'@'localhost';
```

#### Environment Variables
Backend için `.env` dosyası oluşturun:
```env
NODE_ENV=development
PORT=8000
DB_HOST=localhost
DB_PORT=3306
DB_USER=ai_hukuk_user
DB_PASSWORD=secure_password
DB_NAME=ai_hukuk_db
JWT_SECRET=your-super-secret-jwt-key
```

### 3. Servisleri Çalıştırın

#### Backend
```bash
cd backend
npm run dev
```

#### Frontend
```bash
cd frontend
npm run dev
```

### 📍 Erişim URL'leri
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8000/v1
- **Swagger Docs**: http://localhost:8000/swagger-ui/index.html
- **Health Check**: http://localhost:8000/v1/health

## 🐳 Docker ile Çalıştırma

Docker kullanarak tüm sistemi hızlıca ayağa kaldırabilirsiniz.

### Hızlı Başlangıç
```bash
# Deploy script'ini çalıştırılabilir yapın
chmod +x deploy.sh

# Development ortamı
./deploy.sh development up

# Production ortamı
./deploy.sh production up
```

### Manuel Docker Komutları
```bash
# Development
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d

# Production
docker-compose up -d

# Logları görüntüle
docker-compose logs -f

# Servisleri durdur
docker-compose down
```

### Docker Servisleri
- **Frontend**: localhost:3000
- **Backend**: localhost:8000
- **MySQL**: localhost:3307
- **Prometheus**: localhost:9090 (opsiyonel)

## 📡 API Dokümantasyonu

### Authentication Endpoints

#### POST /v1/auth/register
Yeni kullanıcı kaydı
```json
{
  "fullName": "Ahmet Yılmaz",
  "email": "ahmet@example.com", 
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "termsAccepted": "true"
}
```

#### POST /v1/auth/login
Kullanıcı girişi
```json
{
  "email": "ahmet@example.com",
  "password": "SecurePass123!",
  "rememberMe": false
}
```

#### GET /v1/health
Sistem sağlık kontrolü
```json
{
  "success": true,
  "data": {
    "status": "healthy",
    "timestamp": "2024-01-27T10:30:00.000Z",
    "database": {
      "connected": true
    }
  }
}
```

### 📚 Swagger API Dokümantasyonu

**Swagger UI** ile tüm endpoint'leri görüntüleyebilir ve test edebilirsiniz:
- **URL**: http://localhost:8000/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8000/v1/api-docs

Detaylı API dokümantasyonu için: [BACKEND_API_DOCUMENTATION.md](./BACKEND_API_DOCUMENTATION.md)

## 🎨 Frontend Özellikleri

### Sayfa Yapısı
- **Login Sayfası** (`/`) - Sistem girişi
- **Register Sayfası** (`/register`) - Yeni üye kaydı
- **Ana Dashboard** (`/home`) - Ana kontrol paneli
- **AI Asistan** (`/asistan`) - Yapay zeka modülleri
- **Ceza Tahmini** (`/asistan/ceza-tahmini`) - AI ceza tahmin
- **Toplum Hizmeti** (`/asistan/toplum-hizmeti`) - Toplum hizmeti bilgileri
- **Ana Yasa İndir** (`/ana-yasa`) - Mevzuat indirme

### UI/UX Özellikleri
- ✨ **Glassmorphism Design** - Modern cam efekti tasarım
- 🎨 **Gradient Backgrounds** - Dinamik renk geçişleri
- 📱 **Full Responsive** - Mobil uyumlu tasarım
- ⚡ **Fast Loading** - Next.js optimizasyonları
- 🔒 **Secure Forms** - Client-side validation

### Teknoloji Özellikleri
- **Server-Side Rendering** - SEO optimizasyonu
- **TypeScript** - Type safety
- **Client-Side Routing** - Hızlı sayfa geçişleri
- **API Integration** - Backend entegrasyonu
- **Error Handling** - Kapsamlı hata yönetimi

## ⚙️ Backend Özellikleri

### API Özellikleri
- 🔐 **JWT Authentication** - Token tabanlı kimlik doğrulama
- 🛡️ **Security Middleware** - CORS, Helmet, Rate Limiting
- 📊 **Input Validation** - Express Validator
- 🗄️ **Database ORM** - Sequelize MySQL entegrasyonu
- 📝 **Request Logging** - Morgan HTTP logger

### Güvenlik Özellikleri
- **Password Hashing** - bcrypt şifreleme
- **Rate Limiting** - DDoS koruması
- **CORS Protection** - Cross-origin güvenlik
- **Helmet Security** - HTTP header güvenliği
- **Input Sanitization** - SQL injection koruması

### Database Özellikleri
- **MySQL 8.0** - Performanslı ilişkisel veritabanı
- **Sequelize ORM** - Model tabanlı veritabanı işlemleri
- **Connection Pooling** - Veritabanı bağlantı optimizasyonu
- **Migration Support** - Database şema yönetimi

## 📝 Geliştirme Süreci

### Yapılan İşlemler Özeti

#### 1. Proje Altyapısı (Commit 1-5)
- ✅ Comprehensive .gitignore konfigürasyonu
- ✅ Proje roadmap ve TODO listesi oluşturuldu
- ✅ Backend API dokümantasyonu hazırlandı
- ✅ Docker kurulum kılavuzu eklendi
- ✅ Build artifacts hariç tutuldu

#### 2. Backend Geliştirme (Commit 6-10)
- ✅ Package.json ve bağımlılıklar eklendi
- ✅ Express.js sunucu konfigürasyonu
- ✅ MySQL veritabanı entegrasyonu
- ✅ User modeli ve bcrypt şifreleme
- ✅ Authentication middleware stack
- ✅ API route'ları (register/login/health)
- ✅ Database initialization script'leri

#### 3. Frontend Geliştirme (Commit 11-15)
- ✅ Next.js 15 ve React 19 kurulumu
- ✅ TypeScript ve TailwindCSS konfigürasyonu
- ✅ Login/Register sayfaları ve form validation'ı
- ✅ Home dashboard ve glassmorphism tasarım
- ✅ AI Asistan modül sayfaları
- ✅ Ana Yasa indirme modülü
- ✅ API client ve authentication utilities
- ✅ Responsive design ve user experience

#### 4. DevOps ve Deploy (Commit 16-17)
- ✅ Docker Compose production/development setup
- ✅ Multi-stage Dockerfile'lar
- ✅ Deploy script otomasyonu
- ✅ Prometheus monitoring konfigürasyonu
- ✅ Health check endpoint'leri
- ✅ Container network ve volume yönetimi

### Git Commit Geçmişi
Toplam **17 commit** ile profesyonel geliştirme süreci:

```
f507dfd - feat: Son eksik dosyalar eklendi
4bfcdf0 - feat(monitoring): Prometheus monitoring konfigürasyonu eklendi
8fb5e4e - feat(docker): Docker Compose ve deployment script eklendi
ce05b1c - feat(frontend): Dockerfile'lar ve dokümantasyon eklendi
a21a1b4 - feat(frontend): Utility fonksiyonları ve statik dosyalar eklendi
9b79013 - feat(frontend): Modül sayfaları ve API route eklendi
26f5103 - feat(frontend): Ana sayfalar ve authentication UI eklendi
1623da1 - feat(frontend): Frontend build konfigürasyonu eklendi
7e52ab2 - feat(backend): API route'ları ve database init eklendi
4f1ab0f - feat(backend): User modeli ve middleware stack eklendi
a9162c4 - feat(backend): Sunucu ve veritabanı konfigürasyonu eklendi
d363e18 - feat(backend): Package.json ve bağımlılıklar eklendi
509eb12 - docs: Docker kurulum kılavuzu eklendi
53ea004 - docs: Backend API dokümantasyonu eklendi
93af144 - chore: .gitignore dosyasını güncelle
ddf183e - docs: add project roadmap and TODO list
19bc79d - feat: add comprehensive .gitignore
```

### Sistem Mimarisi
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│                 │    │                 │    │                 │
│   Frontend      │    │    Backend      │    │    Database     │
│   (Next.js)     │◄──►│   (Express)     │◄──►│    (MySQL)      │
│   Port: 3000    │    │   Port: 8000    │    │   Port: 3307    │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │     Docker      │
                    │   Environment   │
                    └─────────────────┘
```

## 🔮 Gelecek Planları

### Kısa Vade (1-2 Ay)
- [ ] Email doğrulama sistemi entegrasyonu
- [ ] Şifre sıfırlama fonksiyonu
- [ ] User profil yönetimi sayfası
- [ ] AI Asistan backend entegrasyonu
- [ ] Ceza tahmini algoritması geliştirme

### Orta Vade (3-6 Ay)
- [ ] Redis cache entegrasyonu
- [ ] Nginx reverse proxy kurulumu
- [ ] SSL/TLS sertifika yönetimi
- [ ] Advanced logging sistemi
- [ ] Test suite (Jest/Cypress) eklenmesi
- [ ] CI/CD pipeline kurulumu

### Uzun Vade (6+ Ay)
- [ ] Microservice mimarisine geçiş
- [ ] Kubernetes deployment
- [ ] Advanced AI/ML model entegrasyonu
- [ ] Mobile app (React Native)
- [ ] Advanced analytics dashboard
- [ ] Multi-language support

## 👥 Katkıda Bulunma

1. Repository'yi fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'feat: Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## 📞 İletişim

**Proje Sahibi**: Enes Kaydi  
**Email**: [enes@example.com](mailto:enes@example.com)  
**GitHub**: [@EnesKaydi](https://github.com/EnesKaydi)  
**Repository**: [Ai_Based_Law](https://github.com/EnesKaydi/Ai_Based_Law)

---

<div align="center">

**⚖️ AI Tabanlı Hukuk Sistemi - Modern hukuki süreçler için geliştirilmiştir**

</div>
