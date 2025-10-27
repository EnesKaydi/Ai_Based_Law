import Image from 'next/image';

export default function ToplumHizmetiPage() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-900 via-emerald-900 to-slate-800">
      {/* Hukuk temalı arka plan deseni */}
      <div className="absolute inset-0 bg-[url('/justice-motif.svg')] bg-center bg-no-repeat bg-contain opacity-5"></div>
      
      <div className="relative min-h-screen p-8">
        {/* Üst navigasyon */}
        <div className="mb-8 flex items-center space-x-4">
          <a 
            href="/asistan"
            className="inline-flex items-center text-emerald-400 hover:text-emerald-300 transition-colors"
          >
            ← Asistan Ana Sayfası
          </a>
          <span className="text-slate-500">|</span>
          <a 
            href="/home"
            className="inline-flex items-center text-emerald-400 hover:text-emerald-300 transition-colors"
          >
            Ana Sayfa
          </a>
        </div>

        <div className="max-w-6xl mx-auto">
          {/* Sayfa başlığı */}
          <div className="text-center mb-12">
            <Image
              src="/justice-motif.svg"
              alt="Toplum Hizmeti Sistemi"
              width={100}
              height={100}
              className="mx-auto mb-6 opacity-80"
            />
            <h1 className="text-4xl font-bold text-white mb-4">
              🤝 Toplum Hizmeti Bilgi Sistemi
            </h1>
            <p className="text-xl text-slate-300 max-w-3xl mx-auto">
              Toplum hizmeti cezaları hakkında kapsamlı bilgi ve rehber sistemi
            </p>
          </div>

          {/* Ana bilgi kartları */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-12">
            
            {/* Toplum hizmeti nedir */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
              <div className="text-center mb-6">
                <div className="text-6xl mb-4">📖</div>
                <h3 className="text-2xl font-bold text-white mb-4">
                  Toplum Hizmeti Nedir?
                </h3>
              </div>
              <div className="text-slate-300 space-y-4 text-left">
                <p>
                  <strong className="text-white">Toplum hizmeti cezası</strong>, kişinin topluma yararlı işlerde 
                  ücretsiz olarak çalışması esasına dayanan alternatif bir ceza türüdür.
                </p>
                <p>
                  TCK madde 50'ye göre, belirli koşulları sağlayan suçlarda hapis cezası yerine 
                  uygulanabilir ve toplumsal rehabilitasyonu destekler.
                </p>
                <div className="bg-emerald-500/20 rounded-lg p-4 border border-emerald-500/30">
                  <p className="text-emerald-100">
                    <strong>💡 Amaç:</strong> Suçluyu toplumla bütünleştirmek ve topluma faydalı 
                    kılmak, aynı zamanda ceza sisteminin yükünü hafifletmek.
                  </p>
                </div>
              </div>
            </div>

            {/* Kimler yararlanabilir */}
            <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
              <div className="text-center mb-6">
                <div className="text-6xl mb-4">👥</div>
                <h3 className="text-2xl font-bold text-white mb-4">
                  Kimler Yararlanabilir?
                </h3>
              </div>
              <div className="text-slate-300 space-y-4">
                <div className="space-y-3">
                  <div className="flex items-start space-x-3">
                    <span className="text-green-400 text-lg">✓</span>
                    <span>2 yıl veya daha az hapis cezası alan kişiler</span>
                  </div>
                  <div className="flex items-start space-x-3">
                    <span className="text-green-400 text-lg">✓</span>
                    <span>Daha önce kasıtlı suç işlememiş olanlar</span>
                  </div>
                  <div className="flex items-start space-x-3">
                    <span className="text-green-400 text-lg">✓</span>
                    <span>Suçun ve suçlunun kişiliğinin toplum hizmetine uygun olması</span>
                  </div>
                  <div className="flex items-start space-x-3">
                    <span className="text-green-400 text-lg">✓</span>
                    <span>Mağdurla uzlaşmış olanlar (TCK m.73)</span>
                  </div>
                </div>
                <div className="bg-blue-500/20 rounded-lg p-4 border border-blue-500/30">
                  <p className="text-blue-100">
                    <strong>📋 Not:</strong> Nihai karar mahkemeye aittir ve her vaka özel olarak değerlendirilir.
                  </p>
                </div>
              </div>
            </div>

          </div>

          {/* Toplum hizmeti türleri */}
          <div className="mb-12">
            <h2 className="text-3xl font-bold text-white text-center mb-8">
              🏢 Toplum Hizmeti Türleri
            </h2>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              
              {/* Sağlık hizmetleri */}
              <div className="bg-white/10 backdrop-blur-lg rounded-xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300">
                <div className="text-center">
                  <div className="text-4xl mb-3">🏥</div>
                  <h4 className="text-white font-semibold mb-3">Sağlık Hizmetleri</h4>
                  <p className="text-slate-300 text-sm mb-4">
                    Hastaneler, sağlık ocakları, yaşlı bakım evlerinde yardımcı görevler
                  </p>
                  <div className="text-xs text-slate-400">
                    Temizlik, hasta nakli, genel yardım
                  </div>
                </div>
              </div>

              {/* Eğitim hizmetleri */}
              <div className="bg-white/10 backdrop-blur-lg rounded-xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300">
                <div className="text-center">
                  <div className="text-4xl mb-3">📚</div>
                  <h4 className="text-white font-semibold mb-3">Eğitim Desteği</h4>
                  <p className="text-slate-300 text-sm mb-4">
                    Okullar, kütüphaneler, eğitim merkezlerinde destek hizmetleri
                  </p>
                  <div className="text-xs text-slate-400">
                    Kütüphane, temizlik, eğitim materyali hazırlama
                  </div>
                </div>
              </div>

              {/* Çevre hizmetleri */}
              <div className="bg-white/10 backdrop-blur-lg rounded-xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300">
                <div className="text-center">
                  <div className="text-4xl mb-3">🌱</div>
                  <h4 className="text-white font-semibold mb-4">Çevre Koruma</h4>
                  <p className="text-slate-300 text-sm mb-4">
                    Parklar, doğal alanlar, çevre koruma projelerinde çalışma
                  </p>
                  <div className="text-xs text-slate-400">
                    Ağaçlandırma, park bakımı, temizlik
                  </div>
                </div>
              </div>

              {/* Belediye hizmetleri */}
              <div className="bg-white/10 backdrop-blur-lg rounded-xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300">
                <div className="text-center">
                  <div className="text-4xl mb-3">🏛️</div>
                  <h4 className="text-white font-semibold mb-3">Belediye Hizmetleri</h4>
                  <p className="text-slate-300 text-sm mb-4">
                    Belediye bünyesinde çeşitli kamu hizmetlerinde destek
                  </p>
                  <div className="text-xs text-slate-400">
                    İdari işler, park-bahçe, temizlik
                  </div>
                </div>
              </div>

              {/* Sosyal hizmetler */}
              <div className="bg-white/10 backdrop-blur-lg rounded-xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300">
                <div className="text-center">
                  <div className="text-4xl mb-3">❤️</div>
                  <h4 className="text-white font-semibold mb-3">Sosyal Hizmetler</h4>
                  <p className="text-slate-300 text-sm mb-4">
                    Yardım kuruluşları, sosyal hizmet merkezlerinde destek
                  </p>
                  <div className="text-xs text-slate-400">
                    Yardım dağıtımı, sosyal aktivite desteği
                  </div>
                </div>
              </div>

              {/* Kültür-sanat */}
              <div className="bg-white/10 backdrop-blur-lg rounded-xl p-6 border border-white/20 hover:bg-white/15 transition-all duration-300">
                <div className="text-center">
                  <div className="text-4xl mb-3">🎭</div>
                  <h4 className="text-white font-semibold mb-3">Kültür & Sanat</h4>
                  <p className="text-slate-300 text-sm mb-4">
                    Müzeler, kültür merkezleri, sanat etkinliklerinde yardım
                  </p>
                  <div className="text-xs text-slate-400">
                    Rehberlik, organizasyon desteği, temizlik
                  </div>
                </div>
              </div>

            </div>
          </div>

          {/* Süreç bilgisi */}
          <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20 mb-8">
            <h3 className="text-2xl font-bold text-white text-center mb-8">
              ⏰ Süreç ve Koşullar
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
              <div>
                <h4 className="text-lg font-semibold text-white mb-4 flex items-center">
                  <span className="mr-2">📅</span> Süre Hesaplaması
                </h4>
                <div className="space-y-3 text-slate-300">
                  <p>• <strong>1 gün hapis</strong> = <strong>8 saat toplum hizmeti</strong></p>
                  <p>• <strong>Minimum:</strong> 120 saat (15 gün)</p>
                  <p>• <strong>Maksimum:</strong> 960 saat (120 gün)</p>
                  <p>• <strong>Günlük çalışma:</strong> En fazla 8 saat</p>
                  <p>• <strong>Haftalık çalışma:</strong> En fazla 16 saat</p>
                </div>
              </div>
              
              <div>
                <h4 className="text-lg font-semibold text-white mb-4 flex items-center">
                  <span className="mr-2">📋</span> Genel Koşullar
                </h4>
                <div className="space-y-3 text-slate-300">
                  <p>• Gözetim altında çalışma</p>
                  <p>• Düzenli rapor verme</p>
                  <p>• Çalışma disiplinine uyma</p>
                  <p>• İşverenin talimatlarına uyma</p>
                  <p>• Belirlenen sürede tamamlama</p>
                </div>
              </div>
            </div>
          </div>

          {/* İletişim ve başvuru */}
          <div className="bg-gradient-to-r from-emerald-500/20 to-blue-500/20 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
            <div className="text-center">
              <h3 className="text-2xl font-bold text-white mb-4">
                📞 Başvuru ve Bilgi
              </h3>
              <p className="text-slate-300 mb-6 max-w-2xl mx-auto">
                Toplum hizmeti cezası hakkında detaylı bilgi almak ve başvuru süreçleri için 
                yerel Adalet Komisyonu ve Denetimli Serbestlik Müdürlüğü ile iletişime geçin.
              </p>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="bg-white/10 rounded-xl p-6">
                  <h4 className="text-white font-semibold mb-3">🏛️ Adalet Komisyonu</h4>
                  <p className="text-slate-300 text-sm">
                    Mahkeme kararları ve başvuru süreçleri hakkında bilgi
                  </p>
                </div>
                <div className="bg-white/10 rounded-xl p-6">
                  <h4 className="text-white font-semibold mb-3">👮 Denetimli Serbestlik</h4>
                  <p className="text-slate-300 text-sm">
                    Uygulama süreçleri ve gözetim hizmetleri
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
