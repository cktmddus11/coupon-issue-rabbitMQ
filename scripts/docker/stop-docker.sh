#!/bin/bash

# 현재 스크립트 위치에서 프로젝트 루트로 이동
cd "$(dirname "$0")/../.."

# Docker Compose 중지
echo "Docker Compose 서비스를 중지합니다..."
docker-compose down

# 볼륨도 함께 제거할지 묻기
read -p "RabbitMQ 데이터 볼륨도 제거하시겠습니까? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "볼륨 제거 중..."
    docker-compose down -v
    echo "볼륨이 제거되었습니다."
fi

echo "서비스가 중지되었습니다."