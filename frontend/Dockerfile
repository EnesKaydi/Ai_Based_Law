# 🐳 AI Tabanlı Hukuk Sistemi - Frontend Dockerfile

# Build aşaması için Node.js LTS kullan
FROM node:20-alpine AS builder

# Çalışma dizinini ayarla
WORKDIR /app

# Package dosyalarını kopyala
COPY package*.json ./

# Bağımlılıkları yükle
RUN npm ci

# Kaynak kodunu kopyala
COPY . .

# Next.js uygulamasını build et
RUN npm run build

# Production aşaması için minimal Alpine image
FROM node:20-alpine AS runner

# Güvenlik için root olmayan kullanıcı oluştur
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

# Çalışma dizinini ayarla
WORKDIR /app

# Alpine'e curl ekle (root user iken)
RUN apk add --no-cache curl

# Build edilen dosyaları kopyala
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static

# Dosya sahipliğini nextjs kullanıcısına ver
RUN chown -R nextjs:nodejs /app

# Güvenlik için nextjs kullanıcısına geç
USER nextjs

# Port 3000'i aç
EXPOSE 3000

# Ortam değişkenlerini ayarla
ENV NODE_ENV=production
ENV NEXT_TELEMETRY_DISABLED=1
ENV PORT=3000
ENV HOSTNAME=0.0.0.0

# Sağlık kontrol komutu
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:3000/api/health || exit 1

# Uygulamayı başlat
CMD ["node", "server.js"]

# Metadata etiketleri
LABEL maintainer="AI Hukuk Sistemi Team"
LABEL description="AI Tabanlı Hukuk Sistemi Frontend"
LABEL version="1.0.0"
