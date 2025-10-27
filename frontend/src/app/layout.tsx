import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "AI Tabanlı Hukuk Sistemi | Modern Hukuki Destek Platformu",
  description: "Yapay zeka destekli hukuk sistemi ile ceza tahmini, yasa indirme ve toplum hizmeti bilgilendirme hizmetleri. Modern ve güvenli hukuki destek platformu.",
  keywords: "hukuk, ai, yapay zeka, ceza tahmini, türk ceza kanunu, toplum hizmeti, yasal mevzuat",
  authors: [{ name: "AI Hukuk Sistemi" }],
  viewport: "width=device-width, initial-scale=1",
  robots: "index, follow",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="tr">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        {children}
      </body>
    </html>
  );
}
