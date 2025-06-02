import './globals.css';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: '쿠폰 발급 시스템',
  description: 'RabbitMQ를 활용한 쿠폰 발급 시스템',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ko">
      <body>
        {children}
      </body>
    </html>
  );
}
