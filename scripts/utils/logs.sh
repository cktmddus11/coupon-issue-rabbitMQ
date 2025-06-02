#!/bin/bash

# 현재 스크립트 위치에서 프로젝트 루트로 이동
cd "$(dirname "$0")/../.."

echo "어떤 서비스의 로그를 확인하시겠습니까?"
echo "1) 전체 서비스"
echo "2) RabbitMQ"
echo "3) coupon-api (백엔드)"
echo "4) coupon-web (프론트엔드)"

read -p "선택 (1-4): " choice

case $choice in
    1)
        echo "전체 서비스 로그를 확인합니다..."
        docker-compose logs -f
        ;;
    2)
        echo "RabbitMQ 로그를 확인합니다..."
        docker-compose logs -f rabbitmq
        ;;
    3)
        echo "백엔드 API 로그를 확인합니다..."
        docker-compose logs -f coupon-api
        ;;
    4)
        echo "프론트엔드 로그를 확인합니다..."
        docker-compose logs -f coupon-web
        ;;
    *)
        echo "잘못된 선택입니다."
        exit 1
        ;;
esac