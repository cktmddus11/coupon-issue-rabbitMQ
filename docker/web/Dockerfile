# 빌드 단계
FROM node:20-alpine AS build

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 파일 복사
COPY package*.json ./

# 의존성 설치
RUN npm ci

# 소스 코드 복사
COPY . .

# .env.local 파일 생성 (API URL 설정)
RUN echo "NEXT_PUBLIC_API_URL=http://coupon-api:8080" > .env.local

# Next.js 애플리케이션 빌드
RUN npm run build

# 실행 단계
FROM node:20-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 파일 복사
COPY package*.json ./

# 프로덕션 의존성만 설치
RUN npm ci --omit=dev

# 빌드 결과물 복사
COPY --from=build /app/.next ./.next
COPY --from=build /app/public ./public
COPY --from=build /app/.env.local ./.env.local
COPY --from=build /app/next.config.js ./

# 컨테이너 포트 노출
EXPOSE 3000

# 애플리케이션 실행 명령
CMD ["npm", "start"]
