import "./globals.css";
import type { Metadata } from "next";
import { Navigation } from "@/components/Navigation";

export const metadata: Metadata = {
  title: "쿠폰 발급 시스템",
  description: "RabbitMQ를 활용한 쿠폰 발급 시스템",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ko">
      <body>
        <div className="min-h-screen bg-background">
          <Navigation />
          <main className="container mx-auto px-4 py-8">{children}</main>
        </div>
      </body>
    </html>
  );
}
