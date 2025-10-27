import Image from 'next/image';

export default function CezaTahminiPage() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-900 via-red-900 to-slate-800">
      {/* Hukuk temalı arka plan deseni */}
      <div className="absolute inset-0 bg-[url('/justice-motif.svg')] bg-center bg-no-repeat bg-contain opacity-5"></div>
      
      <div className="relative min-h-screen p-8">
        {/* Üst navigasyon */}
        <div className="mb-8 flex items-center space-x-4">
          <a 
            href="/asistan"
            className="inline-flex items-center text-red-400 hover:text-red-300 transition-colors"
          >
            ← Asistan Ana Sayfası
          </a>
          <span className="text-slate-500">|</span>
          <a 
            href="/home"
            className="inline-flex items-center text-red-400 hover:text-red-300 transition-colors"
          >
            Ana Sayfa
          </a>
        </div>

        <div className="max-w-4xl mx-auto">
          {/* Sayfa başlığı */}
          <div className="text-center mb-12">
            <Image
              src="/justice-motif.svg"
              alt="Ceza Tahmini Sistemi"
              width={100}
              height={100}
              className="mx-auto mb-6 opacity-80"
            />
            <h1 className="text-4xl font-bold text-white mb-4">
              ⚖️ AI Ceza Tahmini Sistemi
            </h1>
            <p className="text-xl text-slate-300 max-w-3xl mx-auto">
              Yapay zeka destekli vaka analiz sistemi ile olası ceza tahminlerini öğrenin
            </p>
          </div>

          {/* Ana form kartı */}
          <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20 mb-8">
            <form className="space-y-8">
              
              {/* Suç türü seçimi */}
              <div>
                <label className="block text-lg font-semibold text-white mb-4">
                  🔍 Suç Türü Seçin
                </label>
                <select className="w-full px-4 py-3 bg-white/10 border border-white/30 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-red-400 focus:border-transparent">
                  <option value="">Suç türünü seçiniz...</option>
                  <option value="hirsizlik">Hırsızlık</option>
                  <option value="dolandiricilik">Dolandırıcılık</option>
                  <option value="yaralama">Yaralama</option>
                  <option value="tehdit">Tehdit</option>
                  <option value="hakaret">Hakaret</option>
                  <option value="uyusturucu">Uyuşturucu</option>
                  <option value="trafik">Trafik Suçları</option>
                  <option value="diger">Diğer</option>
                </select>
              </div>

              {/* Vaka detayları */}
              <div>
                <label className="block text-lg font-semibold text-white mb-4">
                  📝 Vaka Detayları
                </label>
                <textarea
                  rows={6}
                  className="w-full px-4 py-3 bg-white/10 border border-white/30 rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-red-400 focus:border-transparent resize-none"
                  placeholder="Vakanın detaylarını yazın... (Örn: Mağaza rafından 50 TL değerinde ürün çaldı, daha önce sabıka kaydı yok, pişmanlık gösteriyor.)"
                />
              </div>

              {/* Ek faktörler */}
              <div>
                <label className="block text-lg font-semibold text-white mb-4">
                  ⚡ Ek Faktörler
                </label>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  
                  {/* Sabıka durumu */}
                  <div className="bg-white/5 rounded-lg p-4 border border-white/10">
                    <label className="block text-white font-medium mb-2">Sabıka Durumu</label>
                    <select className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded text-white text-sm">
                      <option value="yok">Sabıka kaydı yok</option>
                      <option value="var">Sabıka kaydı var</option>
                      <option value="belirsiz">Belirsiz</option>
                    </select>
                  </div>

                  {/* Yaş grubu */}
                  <div className="bg-white/5 rounded-lg p-4 border border-white/10">
                    <label className="block text-white font-medium mb-2">Yaş Grubu</label>
                    <select className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded text-white text-sm">
                      <option value="reusit">Reşit (18+)</option>
                      <option value="cocuk">Çocuk (15-18)</option>
                      <option value="kucuk">Küçük (12-15)</option>
                    </select>
                  </div>

                  {/* Pişmanlık durumu */}
                  <div className="bg-white/5 rounded-lg p-4 border border-white/10">
                    <label className="block text-white font-medium mb-2">Pişmanlık</label>
                    <select className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded text-white text-sm">
                      <option value="var">Pişmanlık gösteriyor</option>
                      <option value="yok">Pişmanlık göstermiyor</option>
                      <option value="belirsiz">Belirsiz</option>
                    </select>
                  </div>

                  {/* Maddi durum */}
                  <div className="bg-white/5 rounded-lg p-4 border border-white/10">
                    <label className="block text-white font-medium mb-2">Maddi Zarar</label>
                    <input 
                      type="number" 
                      placeholder="TL cinsinden" 
                      className="w-full px-3 py-2 bg-white/10 border border-white/20 rounded text-white text-sm placeholder-white/50"
                    />
                  </div>

                </div>
              </div>

              {/* Analiz butonu */}
              <div className="text-center">
                <button
                  type="submit"
                  className="bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white font-bold py-4 px-8 rounded-lg transition-all duration-200 transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-2 focus:ring-offset-transparent text-lg"
                >
                  🤖 AI Analiz Başlat
                </button>
              </div>

            </form>
          </div>

          {/* Önemli uyarı */}
          <div className="bg-yellow-500/20 backdrop-blur-lg rounded-2xl p-6 border border-yellow-500/30">
            <div className="flex items-start space-x-4">
              <div className="text-yellow-400 text-2xl">⚠️</div>
              <div>
                <h4 className="text-yellow-400 font-bold text-lg mb-2">Önemli Uyarı</h4>
                <p className="text-yellow-100 leading-relaxed">
                  Bu sistem sadece <strong>tahmin ve bilgilendirme</strong> amaçlıdır. 
                  Gerçek hukuki durumlar için mutlaka <strong>avukat danışmanlığı</strong> alın. 
                  AI tahmini resmi hukuki görüş değildir ve mahkeme kararını etkilemez.
                </p>
              </div>
            </div>
          </div>

          {/* Sistem özellikleri */}
          <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-white/5 rounded-xl p-6 text-center border border-white/10">
              <div className="text-3xl mb-3">🧠</div>
              <h5 className="text-white font-semibold mb-2">Akıllı Analiz</h5>
              <p className="text-slate-400 text-sm">Machine learning algoritmaları ile analiz</p>
            </div>
            <div className="bg-white/5 rounded-xl p-6 text-center border border-white/10">
              <div className="text-3xl mb-3">📚</div>
              <h5 className="text-white font-semibold mb-2">Güncel Mevzuat</h5>
              <p className="text-slate-400 text-sm">Türk Ceza Kanunu referansları</p>
            </div>
            <div className="bg-white/5 rounded-xl p-6 text-center border border-white/10">
              <div className="text-3xl mb-3">📊</div>
              <h5 className="text-white font-semibold mb-2">İstatistik</h5>
              <p className="text-slate-400 text-sm">Benzer vakaların analizi</p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
