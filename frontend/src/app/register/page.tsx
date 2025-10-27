'use client';

import Image from 'next/image';
import { useState } from 'react';
import { register } from '@/utils/auth';
import { useRouter } from 'next/navigation';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
    termsAccepted: false
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState<Record<string, string[]>>({});
  const router = useRouter();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    
    // Clear field error when user starts typing
    if (fieldErrors[name]) {
      setFieldErrors(prev => ({
        ...prev,
        [name]: []
      }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setFieldErrors({});

    // Basic client-side validation
    if (formData.password !== formData.confirmPassword) {
      setFieldErrors({ confirmPassword: ['Şifre tekrarı eşleşmiyor'] });
      setLoading(false);
      return;
    }

    if (!formData.termsAccepted) {
      setError('Kullanım şartlarını kabul etmelisiniz');
      setLoading(false);
      return;
    }

    try {
      const result = await register({
        fullName: formData.fullName,
        email: formData.email,
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        termsAccepted: formData.termsAccepted.toString()
      });
      
      if (result.success) {
        console.log('✅ Registration successful:', result.user);
        router.push('/home');
      } else {
        if (result.errors) {
          setFieldErrors(result.errors);
        } else {
          setError(result.message || 'Kayıt işlemi başarısız oldu');
        }
      }
    } catch (error) {
      console.error('Registration error:', error);
      setError('Beklenmeyen bir hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-800">
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
              Yeni hesap oluşturun
            </p>
          </div>

          {/* Kayıt formu kartı */}
          <div className="bg-white/10 backdrop-blur-lg rounded-2xl p-8 border border-white/20">
            {error && (
              <div className="mb-6 p-4 bg-red-500/20 border border-red-500/30 rounded-lg">
                <p className="text-red-300 text-sm">{error}</p>
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Ad Soyad alanı */}
              <div>
                <label htmlFor="fullName" className="block text-sm font-medium text-white mb-2">
                  Ad Soyad
                </label>
                <input
                  id="fullName"
                  name="fullName"
                  type="text"
                  autoComplete="name"
                  required
                  value={formData.fullName}
                  onChange={handleInputChange}
                  className={`w-full px-4 py-3 bg-white/10 border rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-purple-400 focus:border-transparent ${fieldErrors.fullName?.length ? 'border-red-500' : 'border-white/30'}`}
                  placeholder="Adınız Soyadınız"
                />
                {fieldErrors.fullName?.map((error, index) => (
                  <p key={index} className="mt-1 text-sm text-red-300">{error}</p>
                ))}
              </div>

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
                  className={`w-full px-4 py-3 bg-white/10 border rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-purple-400 focus:border-transparent ${fieldErrors.email?.length ? 'border-red-500' : 'border-white/30'}`}
                  placeholder="ornek@email.com"
                />
                {fieldErrors.email?.map((error, index) => (
                  <p key={index} className="mt-1 text-sm text-red-300">{error}</p>
                ))}
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
                  autoComplete="new-password"
                  required
                  value={formData.password}
                  onChange={handleInputChange}
                  className={`w-full px-4 py-3 bg-white/10 border rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-purple-400 focus:border-transparent ${fieldErrors.password?.length ? 'border-red-500' : 'border-white/30'}`}
                  placeholder="••••••••"
                />
                {fieldErrors.password?.map((error, index) => (
                  <p key={index} className="mt-1 text-sm text-red-300">{error}</p>
                ))}
              </div>

              {/* Şifre tekrar alanı */}
              <div>
                <label htmlFor="confirmPassword" className="block text-sm font-medium text-white mb-2">
                  Şifre Tekrar
                </label>
                <input
                  id="confirmPassword"
                  name="confirmPassword"
                  type="password"
                  autoComplete="new-password"
                  required
                  value={formData.confirmPassword}
                  onChange={handleInputChange}
                  className={`w-full px-4 py-3 bg-white/10 border rounded-lg text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-purple-400 focus:border-transparent ${fieldErrors.confirmPassword?.length ? 'border-red-500' : 'border-white/30'}`}
                  placeholder="••••••••"
                />
                {fieldErrors.confirmPassword?.map((error, index) => (
                  <p key={index} className="mt-1 text-sm text-red-300">{error}</p>
                ))}
              </div>

              {/* Kullanım şartları checkbox */}
              <div className="flex items-start">
                <input
                  id="terms"
                  name="termsAccepted"
                  type="checkbox"
                  required
                  checked={formData.termsAccepted}
                  onChange={handleInputChange}
                  className="mt-1 h-4 w-4 text-purple-600 focus:ring-purple-400 border-white/30 rounded bg-white/10"
                />
                <label htmlFor="terms" className="ml-3 text-sm text-slate-300">
                  <span>Kullanım şartlarını ve gizlilik politikasını kabul ediyorum</span>
                </label>
              </div>

              {/* Kayıt ol butonu */}
              <button
                type="submit"
                disabled={loading}
                className={`w-full bg-gradient-to-r from-purple-600 to-purple-700 hover:from-purple-700 hover:to-purple-800 text-white font-semibold py-3 px-6 rounded-lg transition-all duration-200 transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-purple-400 focus:ring-offset-2 focus:ring-offset-transparent ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
              >
                {loading ? '⏳ Kayıt yapılıyor...' : '📝 Kayıt Ol'}
              </button>
            </form>

            {/* Giriş yap linki */}
            <div className="mt-6 text-center">
              <p className="text-slate-300">
                Zaten hesabınız var mı?{' '}
                <a
                  href="/"
                  className="text-purple-400 hover:text-purple-300 font-semibold transition-colors"
                >
                  Giriş Yap
                </a>
              </p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
