# 🐳 Docker Kurulum Kılavuzu

## 📋 Environment Variables

Projeyi çalıştırmadan önce environment dosyalarını oluşturun:

### Production Environment
```bash
# .env.production dosyası oluşturun
cp .env.example .env.production

# Değerleri güncelleyin:
NODE_ENV=production
JWT_SECRET=your-super-secret-jwt-key-change-in-production
DATABASE_URL=postgresql://postgres:secure-password@postgres:5432/ai_hukuk_db
```

### Development Environment  
```bash
# .env.development dosyası oluşturun
NODE_ENV=development
DEBUG=true
NEXT_PUBLIC_API_URL=http://localhost:8000/v1
DATABASE_URL=postgresql://dev_user:dev_password@localhost:5433/ai_hukuk_dev
```

## 🚀 Kurulum ve Çalıştırma

### 1. Hızlı Başlangıç
```bash
# Projeyi klonlayın
git clone <repo-url>
cd Ai_Based_Law

# Deploy script'ini çalıştırılabilir yapın
chmod +x deploy.sh

# Production'da çalıştırın
./deploy.sh production up

# Development'ta çalıştırın  
./deploy.sh development up
```

### 2. Manual Docker Komutları
```bash
# Production
docker-compose up -d --build

# Development
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build

# Durdur
docker-compose down

# Logları görüntüle
docker-compose logs -f frontend
```

## 📊 Servis URL'leri

| Servis | Production | Development |
|--------|------------|-------------|
| Frontend | http://localhost:3000 | http://localhost:3000 |
| Backend API | http://localhost:8000 | http://localhost:8000 |
| PostgreSQL | localhost:5432 | localhost:5433 |
| Redis | localhost:6379 | localhost:6379 |
| Health Check | http://localhost:3000/api/health | - |

## 🔧 Deploy Script Kullanımı

```bash
# Tüm servisleri başlat
./deploy.sh production up

# Servisleri durdur
./deploy.sh production down

# Yeniden başlat
./deploy.sh production restart

# Image'ları yeniden build et
./deploy.sh production build

# Logları göster
./deploy.sh production logs

# Servis durumlarını göster
./deploy.sh production status

# Sistem temizliği
./deploy.sh production clean
```

## 🏥 Sağlık Kontrolleri

### Frontend Health Check
```bash
curl http://localhost:3000/api/health
```

### Container Status
```bash
docker ps
docker-compose ps
```

## 🛠️ Troubleshooting

### Port Çakışması
```bash
# Kullanılan portları kontrol et
lsof -i :3000
lsof -i :8000
lsof -i :5432

# Docker portlarını temizle
docker-compose down
```

### Memory Issues
```bash
# Docker stats
docker stats

# Memory temizliği
docker system prune -f
```

### Build Issues
```bash
# No-cache build
./deploy.sh production build

# Image'ları sil ve yeniden build et
docker rmi $(docker images -q)
./deploy.sh production up
```

## 📦 Production Deployment

### SSL/TLS Kurulumu
1. SSL sertifikalarını `nginx/ssl/` klasörüne koyun
2. `nginx/nginx.conf` dosyasını düzenleyin
3. Environment variables'da SSL_ENABLED=true yapın

### Domain Kurulumu
1. DNS kayıtlarını sunucu IP'sini gösterecek şekilde ayarlayın
2. `docker-compose.yml`'de traefik labels'ları güncelleyin
3. CORS ayarlarını production domain'e göre düzenleyin

### Monitoring
- Prometheus: http://localhost:9090
- Health checks otomatik olarak çalışır
- Loglar `docker-compose logs` ile izlenebilir
