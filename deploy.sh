#!/bin/bash

# 🚀 AI Tabanlı Hukuk Sistemi - Deployment Script
# Bu script uygulamayı Docker ile deploy eder

set -e  # Hata durumunda scripti durdur

# Renkli output için
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logo
echo -e "${BLUE}"
echo "  ⚖️  AI TABANLI HUKUK SİSTEMİ  ⚖️"
echo "     Docker Deployment Script"
echo -e "${NC}"

# Fonksiyonlar
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[WARNING] $1${NC}"
}

error() {
    echo -e "${RED}[ERROR] $1${NC}"
    exit 1
}

# Komut satırı argümanları
ENVIRONMENT=${1:-production}
ACTION=${2:-up}

log "Environment: $ENVIRONMENT"
log "Action: $ACTION"

# Docker kontrolü
if ! command -v docker &> /dev/null; then
    error "Docker yüklü değil. Lütfen Docker'ı yükleyin."
fi

# Docker Compose kontrolü (docker-compose veya docker compose)
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    error "Docker Compose yüklü değil. Lütfen Docker Compose'u yükleyin veya Docker Desktop kullanın."
fi

# Docker Compose komutunu belirle
DOCKER_COMPOSE_CMD="docker-compose"
if ! command -v docker-compose &> /dev/null; then
    if docker compose version &> /dev/null; then
        DOCKER_COMPOSE_CMD="docker compose"
    fi
fi

# Çevresel değişkenler dosyası kontrolü
ENV_FILE=".env.${ENVIRONMENT}"
if [ ! -f "$ENV_FILE" ]; then
    warn "$ENV_FILE dosyası bulunamadı. Varsayılan değerler kullanılacak."
fi

# Proje dizini kontrolü
if [ ! -f "docker-compose.yml" ]; then
    error "docker-compose.yml dosyası bulunamadı. Proje dizininde olduğunuzdan emin olun."
fi

# Ana deploy fonksiyonu
deploy() {
    local env=$1
    local action=$2
    
    case $action in
        "up")
            log "🚀 Uygulamayı başlatıyor..."
            if [ "$env" = "development" ]; then
                $DOCKER_COMPOSE_CMD -f docker-compose.yml -f docker-compose.dev.yml up -d --build
            else
                $DOCKER_COMPOSE_CMD up -d --build
            fi
            ;;
        "down")
            log "🛑 Uygulamayı durduruyor..."
            if [ "$env" = "development" ]; then
                $DOCKER_COMPOSE_CMD -f docker-compose.yml -f docker-compose.dev.yml down
            else
                $DOCKER_COMPOSE_CMD down
            fi
            ;;
        "restart")
            log "🔄 Uygulamayı yeniden başlatıyor..."
            $0 $env down
            sleep 2
            $0 $env up
            ;;
        "build")
            log "🔨 Image'ları yeniden build ediyor..."
            if [ "$env" = "development" ]; then
                $DOCKER_COMPOSE_CMD -f docker-compose.yml -f docker-compose.dev.yml build --no-cache
            else
                $DOCKER_COMPOSE_CMD build --no-cache
            fi
            ;;
        "logs")
            log "📋 Logları gösteriyor..."
            if [ "$env" = "development" ]; then
                $DOCKER_COMPOSE_CMD -f docker-compose.yml -f docker-compose.dev.yml logs -f
            else
                $DOCKER_COMPOSE_CMD logs -f
            fi
            ;;
        "status")
            log "📊 Servis durumları:"
            if [ "$env" = "development" ]; then
                $DOCKER_COMPOSE_CMD -f docker-compose.yml -f docker-compose.dev.yml ps
            else
                $DOCKER_COMPOSE_CMD ps
            fi
            ;;
        "clean")
            log "🧹 Temizlik yapıyor..."
            if [ "$env" = "development" ]; then
                $DOCKER_COMPOSE_CMD -f docker-compose.yml -f docker-compose.dev.yml down -v
            else
                $DOCKER_COMPOSE_CMD down -v
            fi
            docker system prune -f
            docker volume prune -f
            ;;
        *)
            error "Geçersiz action: $action. Kullanılabilir: up, down, restart, build, logs, status, clean"
            ;;
    esac
}

# Sağlık kontrolü
health_check() {
    log "🏥 Sağlık kontrolü yapılıyor..."
    
    # Frontend sağlık kontrolü
    max_attempts=30
    attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f http://localhost:3000/api/health &> /dev/null; then
            log "✅ Frontend sağlıklı!"
            break
        else
            if [ $attempt -eq $max_attempts ]; then
                warn "❌ Frontend sağlık kontrolü başarısız!"
            else
                echo -n "."
                sleep 2
                ((attempt++))
            fi
        fi
    done
    
    # Database bağlantı kontrolü (backend hazır olduğunda)
    # Backend sağlık kontrolü burada eklenecek
}

# Pre-deployment kontrolleri
pre_deploy_checks() {
    log "🔍 Pre-deployment kontrolleri..."
    
    # Disk alanı kontrolü
    available_space=$(df / | awk 'NR==2 {print $4}')
    required_space=1000000  # 1GB in KB
    
    if [ "$available_space" -lt "$required_space" ]; then
        warn "Disk alanı yetersiz olabilir. Kullanılabilir: ${available_space}KB"
    fi
    
    # Memory kontrolü
    available_memory=$(free -m | awk 'NR==2{print $7}')
    required_memory=512  # 512MB
    
    if [ "$available_memory" -lt "$required_memory" ]; then
        warn "Bellek yetersiz olabilir. Kullanılabilir: ${available_memory}MB"
    fi
    
    log "✅ Pre-deployment kontrolleri tamamlandı"
}

# Ana script akışı
main() {
    case $ACTION in
        "up")
            pre_deploy_checks
            deploy $ENVIRONMENT $ACTION
            if [ "$ENVIRONMENT" = "production" ]; then
                health_check
            fi
            ;;
        *)
            deploy $ENVIRONMENT $ACTION
            ;;
    esac
    
    log "🎉 İşlem tamamlandı!"
    
    if [ "$ACTION" = "up" ]; then
        echo -e "${BLUE}"
        echo "📱 Frontend: http://localhost:3000"
        echo "🔗 Backend API: http://localhost:8000"
        echo "🗄️  PostgreSQL: localhost:5432"
        echo "🔴 Redis: localhost:6379"
        echo -e "${NC}"
    fi
}

# Script'i çalıştır
main

# Kullanım örnekleri:
# ./deploy.sh production up          # Production'da başlat
# ./deploy.sh development up         # Development'ta başlat
# ./deploy.sh production down        # Uygulamayı durdur
# ./deploy.sh production restart     # Yeniden başlat
# ./deploy.sh production build       # Yeniden build et
# ./deploy.sh production logs        # Logları göster
# ./deploy.sh production status      # Durumu göster
# ./deploy.sh production clean       # Temizlik yap
