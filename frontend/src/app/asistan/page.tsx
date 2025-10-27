import Image from 'next/image';

export default function AsistanPage() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-800">
      {/* Hukuk temalı arka plan deseni */}
      <div className="absolute inset-0 bg-[url('/justice-motif.svg')] bg-center bg-no-repeat bg-contain opacity-5"></div>
      
      <div className="relative min-h-screen p-8">
        {/* Üst navigasyon */}
        <div className="mb-8">
          <a 
            href="/home"
            className="inline-flex items-center text-blue-400 hover:text-blue-300 transition-colors"
          >
            ← Ana Sayfaya Dön
          </a>
        </div>

        <div className="max-w-4xl mx-auto">
          {/* Sayfa başlığı */}
          <div className="text-center mb-12">
            <Image
              src="/justice-motif.svg"
              alt="AI Asistan Logosu"
              width={100}
              height={100}
              className="mx-auto mb-6 opacity-80"
            />
            <h1 className="text-4xl font-bold text-white mb-4">
              🤖 AI Hukuk Asistanı
            </h1>
            <p className="text-xl text-slate-300 max-w-2xl mx-auto">
              Yapay zeka destekli hukuki destek sistemine hoş geldiniz
            </p>
          </div>

          {/* Asistan modülleri */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            
            {/* Ceza Tahmini Modülü */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-8xl mb-6">⚖️</div>
                <h3 className="text-2xl font-bold text-white mb-4">
                  Ceza Tahmini Sistemi
                </h3>
                <p className="text-slate-300 mb-8 leading-relaxed">
                  AI ile vaka analizi yapın ve olası ceza tahminleri alın. 
                  Machine learning algoritmaları ile desteklenen akıllı analiz sistemi.
                </p>
                
                {/* Özellikler listesi */}
                <div className="text-left mb-8 space-y-2">
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    Akıllı vaka analizi
                  </div>
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    Türk Ceza Kanunu referansları
                  </div>
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    İstatistiksel tahminler
                  </div>
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    Emsal karar analizleri
                  </div>
                </div>

                <a 
                  href="/asistan/ceza-tahmini"
                  className="block w-full bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white font-semibold py-4 px-6 rounded-lg transition-all duration-200 transform hover:scale-105"
                >
                  🔍 Ceza Tahmini Başlat
                </a>
              </div>
            </div>

            {/* Toplum Hizmeti Modülü */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-8xl mb-6">🤝</div>
                <h3 className="text-2xl font-bold text-white mb-4">
                  Toplum Hizmeti Sistemi
                </h3>
                <p className="text-slate-300 mb-8 leading-relaxed">
                  Toplum hizmeti cezalarını görüntüleyin ve takip edin. 
                  Alternatif ceza seçenekleri hakkında bilgi alın.
                </p>
                
                {/* Özellikler listesi */}
                <div className="text-left mb-8 space-y-2">
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    Toplum hizmeti türleri
                  </div>
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    Süre ve koşul bilgileri
                  </div>
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    İlgili kurum listesi
                  </div>
                  <div className="flex items-center text-slate-300">
                    <span className="text-green-400 mr-2">✓</span>
                    Başvuru rehberi
                  </div>
                </div>

                <a 
                  href="/asistan/toplum-hizmeti"
                  className="block w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-4 px-6 rounded-lg transition-all duration-200 transform hover:scale-105"
                >
                  🤝 Toplum Hizmeti Görüntüle
                </a>
              </div>
            </div>

          </div>

          {/* Alt bilgi kartı */}
          <div className="mt-12">
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20">
              <div className="text-center">
                <h4 className="text-xl font-semibold text-white mb-3">
                  🛡️ Güvenli ve Gizli
                </h4>
                <p className="text-slate-300 mb-4">
                  Tüm verileriniz güvenli şekilde işlenir ve gizliliğiniz korunur. 
                  AI asistan sadece genel hukuki bilgiler sunar, resmi hukuki tavsiye değildir.
                </p>
                <div className="flex items-center justify-center space-x-6 text-sm text-slate-400">
                  <span>🔐 KVKK Uyumlu</span>
                  <span>•</span>
                  <span>🛡️ SSL Güvenlik</span>
                  <span>•</span>
                  <span>⚡ 7/24 Aktif</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
