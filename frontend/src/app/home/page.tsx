import Image from 'next/image';

export default function Home() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-800">
      {/* Hukuk temalı arka plan deseni */}
      <div className="absolute inset-0 bg-[url('/justice-motif.svg')] bg-center bg-no-repeat bg-contain opacity-5"></div>
      
      <div className="relative min-h-screen p-8">
        
        {/* Üst navigasyon / Çıkış butonu */}
        <div className="flex justify-end mb-8 space-x-4">
          <a 
            href="/"
            className="bg-red-500/20 backdrop-blur-lg px-6 py-2 rounded-lg text-red-300 border border-red-500/30 hover:bg-red-500/30 transition-all duration-200"
          >
            🚪 Çıkış Yap
          </a>
        </div>

        <div className="max-w-6xl mx-auto">
          {/* Ana başlık */}
          <div className="text-center mb-16">
            <Image
              src="/justice-motif.svg"
              alt="AI Hukuk Sistemi Logosu"
              width={100}
              height={100}
              className="mx-auto mb-8 opacity-90"
            />
            
            <div className="bg-gradient-to-r from-slate-800/50 to-blue-900/50 backdrop-blur-lg rounded-2xl p-8 border border-slate-700/50 mb-8">
              <h1 className="text-5xl font-bold text-white mb-6">
                AI Tabanlı Hukuk Sistemi
              </h1>
              <p className="text-xl text-slate-300 max-w-3xl mx-auto leading-relaxed">
                <strong>Türkiye Cumhuriyeti</strong> yasalarına dayalı yapay zeka destekli modern hukuk platformu. 
                <br />
                <em className="text-blue-300">Adalet, Eşitlik ve Hukuk Devleti İlkeleri</em> doğrultusunda hizmet vermektedir.
              </p>
            </div>
          </div>

          {/* Ana modüller grid */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-12">
            
            {/* Ana Yasa İndir Modülü */}
            <div className="bg-gradient-to-br from-slate-800/60 to-emerald-900/60 backdrop-blur-lg rounded-2xl p-8 border border-slate-700/50 hover:border-emerald-500/50 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-5xl mb-6">📜</div>
                <h2 className="text-3xl font-bold text-white mb-4">
                  Ana Yasa İndirme Merkezi
                </h2>
                <div className="bg-emerald-900/40 rounded-lg p-4 mb-6 border border-emerald-500/30">
                  <p className="text-emerald-100 font-semibold">
                    RESMİ YASAL MEVZUAT
                  </p>
                  <p className="text-emerald-200 text-sm mt-1">
                    T.C. Resmi Gazete'de Yayınlanan Kanunlar
                  </p>
                </div>
                <p className="text-slate-300 mb-8 leading-relaxed">
                  <strong>Türkiye Cumhuriyeti</strong> ana yasalarını ve güncel mevzuatları kolayca indirin. 
                  <br />
                  Anayasa, TCK, CMK ve diğer <em>temel kanunlar</em> tek tıkla erişiminizde.
                </p>
                
                {/* Alt özellikler */}
                <div className="grid grid-cols-2 gap-4 mb-8 text-sm">
                  <div className="bg-white/10 rounded-lg p-3 border border-white/10">
                    <div className="text-emerald-400 font-semibold">T.C. Anayasası</div>
                  </div>
                  <div className="bg-white/10 rounded-lg p-3 border border-white/10">
                    <div className="text-red-400 font-semibold">Türk Ceza Kanunu</div>
                  </div>
                  <div className="bg-white/10 rounded-lg p-3 border border-white/10">
                    <div className="text-blue-400 font-semibold">Medeni Kanun</div>
                  </div>
                  <div className="bg-white/10 rounded-lg p-3 border border-white/10">
                    <div className="text-purple-400 font-semibold">İş Kanunu</div>
                  </div>
                </div>

                <a 
                  href="/ana-yasa"
                  className="block w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-bold py-4 px-6 rounded-lg transition-all duration-200 transform hover:scale-105"
                >
                  Yasaları İndir
                </a>
              </div>
            </div>

            {/* AI Hukuk Asistanı Modülü */}
            <div className="bg-gradient-to-br from-slate-800/60 to-blue-900/60 backdrop-blur-lg rounded-2xl p-8 border border-slate-700/50 hover:border-blue-500/50 transition-all duration-300 transform hover:scale-105">
              <div className="text-center">
                <div className="text-5xl mb-6">🤖</div>
                <h2 className="text-3xl font-bold text-white mb-4">
                  AI Hukuk Asistanı
                </h2>
                <div className="bg-blue-900/40 rounded-lg p-4 mb-6 border border-blue-500/30">
                  <p className="text-blue-100 font-semibold">
                    YAPAY ZEKA HUKUKİ DANIŞMAN
                  </p>
                  <p className="text-blue-200 text-sm mt-1">
                    Türk Hukuk Sistemi Tabanlı AI Analiz
                  </p>
                </div>
                <p className="text-slate-300 mb-8 leading-relaxed">
                  <strong>Yapay zeka destekli hukuk asistanı</strong> ile ceza tahmini yapın ve 
                  <br />
                  <em>Toplum hizmeti seçeneklerini</em> keşfedin. Akıllı analiz araçları.
                </p>
                
                {/* Alt modüller */}
                <div className="space-y-4 mb-8">
                  <div className="bg-red-500/20 rounded-lg p-4 border border-red-500/30">
                    <div className="text-center">
                      <div className="text-white font-semibold">Ceza Tahmini Sistemi</div>
                      <div className="text-red-200 text-sm">AI ile akıllı vaka analizi</div>
                    </div>
                  </div>
                  
                  <div className="bg-emerald-500/20 rounded-lg p-4 border border-emerald-500/30">
                    <div className="text-center">
                      <div className="text-white font-semibold">Toplum Hizmeti Bilgileri</div>
                      <div className="text-emerald-200 text-sm">Alternatif ceza seçenekleri</div>
                    </div>
                  </div>
                </div>

                <a 
                  href="/asistan"
                  className="block w-full bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white font-bold py-4 px-6 rounded-lg transition-all duration-200 transform hover:scale-105"
                >
                  Asistanı Başlat
                </a>
              </div>
            </div>

          </div>

          {/* Alt bilgi ve özellikler */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
            <div className="bg-slate-800/60 backdrop-blur-lg rounded-xl p-6 text-center border border-slate-700/50">
              <h3 className="text-white font-semibold mb-2">Güvenli ve Gizli</h3>
              <p className="text-slate-300 text-sm">KVKK uyumlu veri koruma ve SSL güvenlik</p>
              <p className="text-slate-400 text-xs mt-2">Kişisel Verilerin Korunması</p>
            </div>
            
            <div className="bg-slate-800/60 backdrop-blur-lg rounded-xl p-6 text-center border border-slate-700/50">
              <h3 className="text-white font-semibold mb-2">AI Hukuk Destekli</h3>
              <p className="text-slate-300 text-sm">Machine learning ile akıllı hukuk analizi</p>
              <p className="text-slate-400 text-xs mt-2">Türk Hukuk Sistemi Tabanlı</p>
            </div>
            
            <div className="bg-slate-800/60 backdrop-blur-lg rounded-xl p-6 text-center border border-slate-700/50">
              <h3 className="text-white font-semibold mb-2">Güncel T.C. Mevzuatı</h3>
              <p className="text-slate-300 text-sm">Sürekli güncellenen yasal referanslar</p>
              <p className="text-slate-400 text-xs mt-2">Resmi Gazete Takipli</p>
            </div>
          </div>

          {/* Önemli uyarı */}
          <div className="bg-gradient-to-r from-red-500/20 to-orange-500/20 backdrop-blur-lg rounded-2xl p-6 border border-red-500/30">
            <div className="text-center">
              <h4 className="text-red-300 font-bold text-lg mb-3">
                HUKUKİ UYARI
              </h4>
              <p className="text-red-100 leading-relaxed">
                Bu platform <strong>bilgilendirme ve tahmin</strong> amaçlıdır. 
                <br />
                Resmi hukuki işlemler için mutlaka <strong>avukat danışmanlığı</strong> alın.
                <br />
                <em className="text-red-200 text-sm">"Hukuk önünde herkes eşittir" - T.C. Anayasası</em>
              </p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
