#!/bin/bash

# 현재 스크립트 위치에서 프로젝트 루트로 이동
cd "$(dirname "$0")/../.."

# Docker Compose 실행
echo "Docker Compose로 쿠폰 발급 시스템을 시작합니다..."
docker-compose up -d

echo ""
echo "===== 서비스가 시작되었습니다 ====="
echo "RabbitMQ 관리자 UI: http://localhost:15672 (guest/guest)"
echo "쿠폰 발급 시스템 UI: http://localhost:3000"
echo "백엔드 API: http://localhost:8080"
echo "Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo "H2 Console: http://localhost:8080/h2-console"
echo ""
echo "서비스 상태 확인: docker-compose ps"
echo "로그 확인: docker-compose logs -f [service_name]"