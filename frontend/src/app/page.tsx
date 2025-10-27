'use client';

import Image from 'next/image';
import { useState } from 'react';
import { login } from '@/utils/auth';
import { useRouter } from 'next/navigation';

export default function LoginPage() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    rememberMe: false
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const router = useRouter();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const result = await login(formData.email, formData.password, formData.rememberMe);
      
      if (result.success) {
        console.log('✅ Login successful:', result.user);
        router.push('/home');
      } else {
        setError(result.message || 'Giriş işlemi başarısız oldu');
      }
    } catch (error) {
      console.error('Login error:', error);
      setError('Beklenmeyen bir hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  const handleTestLogin = () => {
    router.push('/home');
  };

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
            {error && (
              <div className="mb-6 p-4 bg-red-500/20 border border-red-500/30 rounded-lg">
                <p className="text-red-300 text-sm">{error}</p>
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-6">
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
                  value={formData.email}
                  onChange={handleInputChange}
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
                  value={formData.password}
                  onChange={handleInputChange}
                  className="w-full px-4 py-3 bg-white/10 border border-white/30 rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-transparent"
                  placeholder="••••••••"
                />
              </div>

              {/* Remember me checkbox */}
              <div className="flex items-center">
                <input
                  id="rememberMe"
                  name="rememberMe"
                  type="checkbox"
                  checked={formData.rememberMe}
                  onChange={handleInputChange}
                  className="h-4 w-4 text-blue-600 focus:ring-blue-400 border-white/30 rounded bg-white/10"
                />
                <label htmlFor="rememberMe" className="ml-2 text-sm text-slate-300">
                  Beni hatırla
                </label>
              </div>

              {/* Giriş butonu */}
              <button
                type="submit"
                disabled={loading}
                className={`w-full bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200 transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2 focus:ring-offset-transparent ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
              >
                {loading ? '⏳ Giriş yapılıyor...' : '🔑 Giriş Yap'}
              </button>

              {/* Test giriş butonu */}
              <button
                type="button"
                onClick={handleTestLogin}
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
