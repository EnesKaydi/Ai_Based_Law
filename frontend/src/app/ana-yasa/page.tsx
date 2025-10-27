import Image from 'next/image';

export default function AnaYasaPage() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-900 via-emerald-900 to-slate-800">
      {/* Hukuk temalı arka plan deseni */}
      <div className="absolute inset-0 bg-[url('/justice-motif.svg')] bg-center bg-no-repeat bg-contain opacity-5"></div>
      
      <div className="relative min-h-screen p-8">
        {/* Üst navigasyon */}
        <div className="mb-8">
          <a 
            href="/home"
            className="inline-flex items-center text-emerald-400 hover:text-emerald-300 transition-colors"
          >
            ← Ana Sayfaya Dön
          </a>
        </div>

        <div className="max-w-6xl mx-auto">
          {/* Sayfa başlığı */}
          <div className="text-center mb-12">
            <Image
              src="/justice-motif.svg"
              alt="Adalet Logosu"
              width={100}
              height={100}
              className="mx-auto mb-6 opacity-80"
            />
            <h1 className="text-4xl font-bold text-white mb-4">
              📜 Ana Yasa İndirme Merkezi
            </h1>
            <p className="text-xl text-slate-300 max-w-2xl mx-auto">
              Türkiye Cumhuriyeti ana yasalarını ve mevzuatlarını indirin
            </p>
          </div>

          {/* Yasa kategorileri grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            
            {/* Anayasa */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-6xl mb-4">🏛️</div>
                <h3 className="text-2xl font-bold text-white mb-3">
                  T.C. Anayasası
                </h3>
                <p className="text-slate-300 mb-6">
                  1982 Türkiye Cumhuriyeti Anayasası ve değişiklikleri
                </p>
                <button className="w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200">
                  📥 İndir (PDF)
                </button>
              </div>
            </div>

            {/* Türk Ceza Kanunu */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-6xl mb-4">⚖️</div>
                <h3 className="text-2xl font-bold text-white mb-3">
                  Türk Ceza Kanunu
                </h3>
                <p className="text-slate-300 mb-6">
                  5237 sayılı Türk Ceza Kanunu ve güncel değişiklikleri
                </p>
                <button className="w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200">
                  📥 İndir (PDF)
                </button>
              </div>
            </div>

            {/* Ceza Muhakeme Kanunu */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-6xl mb-4">🔍</div>
                <h3 className="text-2xl font-bold text-white mb-3">
                  Ceza Muhakeme Kanunu
                </h3>
                <p className="text-slate-300 mb-6">
                  5271 sayılı Ceza Muhakemesi Kanunu
                </p>
                <button className="w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200">
                  📥 İndir (PDF)
                </button>
              </div>
            </div>

            {/* Türk Medeni Kanunu */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-6xl mb-4">👥</div>
                <h3 className="text-2xl font-bold text-white mb-3">
                  Türk Medeni Kanunu
                </h3>
                <p className="text-slate-300 mb-6">
                  4721 sayılı Türk Medeni Kanunu
                </p>
                <button className="w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200">
                  📥 İndir (PDF)
                </button>
              </div>
            </div>

            {/* Borçlar Kanunu */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-6xl mb-4">📋</div>
                <h3 className="text-2xl font-bold text-white mb-3">
                  Türk Borçlar Kanunu
                </h3>
                <p className="text-slate-300 mb-6">
                  6098 sayılı Türk Borçlar Kanunu
                </p>
                <button className="w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200">
                  📥 İndir (PDF)
                </button>
              </div>
            </div>

            {/* İş Kanunu */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-6xl mb-4">🏢</div>
                <h3 className="text-2xl font-bold text-white mb-3">
                  İş Kanunu
                </h3>
                <p className="text-slate-300 mb-6">
                  4857 sayılı İş Kanunu
                </p>
                <button className="w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200">
                  📥 İndir (PDF)
                </button>
              </div>
            </div>

          </div>

          {/* Alt bilgi */}
          <div className="mt-12 text-center">
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-6 border border-white/20">
              <p className="text-slate-300 mb-2">
                📅 Yasalar güncel mevzuat değişiklikleri ile birlikte sunulmaktadır
              </p>
              <p className="text-sm text-slate-400">
                Son güncelleme: {new Date().toLocaleDateString('tr-TR')}
              </p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
