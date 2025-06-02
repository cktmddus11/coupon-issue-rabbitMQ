/** @type {import('next').NextConfig} */
const nextConfig = {
  // Next.js 15 설정
  output: "standalone",

  // API 요청 프록시 설정
  async rewrites() {
    return [
      {
        // API 요청을 Spring Boot 서버로 프록시
        source: "/api/:path*",
        destination: `${process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"}/api/:path*`,
      },
    ];
  },
};

module.exports = nextConfig;
