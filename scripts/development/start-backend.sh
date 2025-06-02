#!/bin/bash

# 현재 스크립트 위치에서 프로젝트 루트로 이동
cd "$(dirname "$0")/../.."

echo "백엔드 애플리케이션을 시작합니다..."

# RabbitMQ가 실행 중인지 확인
if ! docker ps | grep -q rabbitmq; then
    echo "RabbitMQ가 실행 중이지 않습니다. RabbitMQ를 먼저 시작하세요."
    echo "scripts/development/start-rabbitmq.sh 를 실행하세요."
    exit 1
fi

# Gradle을 사용하여 백엔드 실행
echo "Gradle로 coupon-api 모듈을 실행합니다..."
./gradlew :coupon-api:bootRun