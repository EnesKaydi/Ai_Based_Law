# 🏛️ AI Tabanlı Hukuk Sistemi - Frontend

Modern ve kullanıcı dostu hukuk sistemi frontend uygulaması.

## 🚀 Özellikler

- ⚖️ **Modern Hukuk Teması**: Glassmorphism tasarım ile hukuki vurgu
- 🔐 **Kimlik Doğrulama**: Email doğrulamalı kayıt/giriş sistemi
- 📜 **Yasa İndirme**: T.C. mevzuatları için indirme merkezi
- 🤖 **AI Asistan**: Yapay zeka destekli hukuk danışmanlığı
- ⚖️ **Ceza Tahmini**: AI ile vaka analizi ve ceza tahmini
- 🤝 **Toplum Hizmeti**: Alternatif ceza seçenekleri bilgilendirme
- 📱 **Responsive**: Mobil uyumlu tasarım

## 🛠️ Teknolojiler

- **Framework**: Next.js 15.3.2
- **UI**: React 19 + TypeScript
- **Styling**: Tailwind CSS 4
- **Icons**: Emoji + SVG
- **Deployment**: Docker destekli

## 📁 Proje Yapısı

```
frontend/
├── src/
│   ├── app/                 # Next.js App Router
│   │   ├── page.tsx         # Giriş sayfası (ana route)
│   │   ├── home/            # Ana sayfa (/home)
│   │   ├── register/        # Kayıt sayfası
│   │   ├── ana-yasa/        # Yasa indirme
│   │   └── asistan/         # AI asistan modülleri
│   │       ├── page.tsx     # Asistan ana sayfası
│   │       ├── ceza-tahmini/    # Ceza tahmini sistemi
│   │       └── toplum-hizmeti/  # Toplum hizmeti bilgileri
│   ├── components/          # Yeniden kullanılabilir bileşenler
│   ├── utils/              # Yardımcı fonksiyonlar
│   └── types/              # TypeScript tip tanımları
├── public/                 # Static dosyalar
│   ├── justice-motif.svg   # Hukuk logosu
└── package.json
```

## 🎨 Routing Sistemi

| Route | Açıklama |
|-------|----------|
| `/` | Giriş/Kayıt sayfası (ilk karşılama) |
| `/home` | Ana sayfa (giriş sonrası) |
| `/register` | Kullanıcı kayıt formu |
| `/ana-yasa` | Yasal doküman indirme merkezi |
| `/asistan` | AI hukuk asistanı ana sayfası |
| `/asistan/ceza-tahmini` | AI ceza tahmini sistemi |
| `/asistan/toplum-hizmeti` | Toplum hizmeti bilgilendirme |

## 🚀 Kurulum ve Çalıştırma

### Geliştirme Ortamı

```bash
# Bağımlılıkları yükle
npm install

# Development server'ı başlat
npm run dev

# Tarayıcıda aç: http://localhost:3000
```

### Production Build

```bash
# Build al
npm run build

# Production server'ı başlat
npm start
```

## 🎨 Tasarım Temaları

### Ana Renkler
- **Primary**: Mavi gradyanlar (adalet teması)
- **Secondary**: Yeşil (yasalar), Kırmızı (ceza), Mor (kayıt)
- **Accent**: Altın/Amber (hukuk vurgusu)

### Hukuki İkonlar
- ⚖️ Terazi (adalet)
- 🏛️ Mahkeme binası
- 📜 Yasal dokümanlar
- 🔨 Mahkeme tokmağı
- 📖 Yasa kitabı
- 🇹🇷 Türkiye bayrağı

## 🔐 Güvenlik

- **HTTPS**: SSL/TLS güvenlik
- **CORS**: Cross-origin koruma
- **Rate Limiting**: API spam koruması
- **Input Validation**: XSS koruması
- **JWT**: Güvenli oturum yönetimi

## 📱 Responsive Tasarım

- **Mobile First**: Mobil öncelikli tasarım
- **Breakpoints**: Tailwind CSS standartları
- **Touch Friendly**: Dokunmatik uyumlu
- **Performance**: Optimize edilmiş yükleme

## 🧪 Test

```bash
# Linting
npm run lint

# Test suite (gelecekte eklenecek)
npm run test
```

## 📄 Lisans

© 2024 AI Tabanlı Hukuk Sistemi. Tüm hakları saklıdır.

---

**Not**: Bu frontend uygulaması bilgilendirme amaçlıdır. Resmi hukuki işlemler için avukat danışmanlığı alın.