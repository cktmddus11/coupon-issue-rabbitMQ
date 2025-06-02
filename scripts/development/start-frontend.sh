#!/bin/bash

# 현재 스크립트 위치에서 프로젝트 루트로 이동
cd "$(dirname "$0")/../.."

# coupon-web 디렉토리로 이동
cd coupon-web

# 의존성 설치 확인
if [ ! -d "node_modules" ]; then
    echo "Node.js 의존성을 설치합니다..."
    npm install
fi

# 백엔드 서버 확인
echo "백엔드 서버 연결을 확인합니다..."
if ! curl -f http://localhost:8080/actuator/health 2>/dev/null; then
    echo "경고: 백엔드 서버(localhost:8080)에 연결할 수 없습니다."
    echo "백엔드 서버를 먼저 시작하세요."
fi

# Next.js 개발 서버 시작
echo "Next.js 개발 서버를 시작합니다..."
npm run dev