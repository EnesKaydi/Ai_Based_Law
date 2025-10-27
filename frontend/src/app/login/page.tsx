import Image from 'next/image';

export default function LoginPage() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-900 via-blue-900 to-slate-800">
      {/* Hukuk temalı arka plan deseni */}
      <div className="absolute inset-0 bg-[url('/justice-motif.svg')] bg-center bg-no-repeat bg-contain opacity-5"></div>
      
      <div className="relative flex min-h-screen items-center justify-center p-8">
        <div className="w-full max-w-md">
          {/* Ana başlık ve logo */}
          <div className="text-center mb-8">
            <Image
              src="/justice-motif.svg"
              alt="Adalet Logosu"
              width={80}
              height={80}
              className="mx-auto mb-4 opacity-80"
            />
            <h1 className="text-3xl font-bold text-white mb-2">
              🏛️ AI Hukuk Sistemi
            </h1>
            <p className="text-slate-300">
              Sisteme giriş yapın
            </p>
          </div>

          {/* Giriş formu kartı */}
          <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
            <form className="space-y-6">
              {/* Email alanı */}
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-white mb-2">
                  Email Adresi
                </label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  className="w-full px-4 py-3 bg-white/10 border border-white/30 rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent"
                  placeholder="ornek@email.com"
                />
              </div>

              {/* Şifre alanı */}
              <div>
                <label htmlFor="password" className="block text-sm font-medium text-white mb-2">
                  Şifre
                </label>
                <input
                  id="password"
                  name="password"
                  type="password"
                  autoComplete="current-password"
                  required
                  className="w-full px-4 py-3 bg-white/10 border border-white/30 rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent"
                  placeholder="••••••••"
                />
              </div>

              {/* Giriş butonu */}
              <button
                type="submit"
                className="w-full bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200 transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2 focus:ring-offset-transparent"
              >
                🔑 Giriş Yap
              </button>

              {/* Test giriş butonu - Backend hazır olmadığı için */}
              <button
                type="button"
                className="w-full bg-gradient-to-r from-emerald-600 to-emerald-700 hover:from-emerald-700 hover:to-emerald-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200 transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-emerald-400 focus:ring-offset-2 focus:ring-offset-transparent"
              >
                🧪 Test Girişi (Geçici)
              </button>
            </form>

            {/* Kayıt ol linki */}
            <div className="mt-6 text-center">
              <p className="text-slate-300">
                Hesabınız yok mu?{' '}
                <a
                  href="/register"
                  className="text-blue-400 hover:text-blue-300 font-semibold transition-colors"
                >
                  Kayıt Ol
                </a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
