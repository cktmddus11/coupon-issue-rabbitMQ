#!/bin/bash

echo "RabbitMQ를 Docker로 시작합니다..."

# 기존 RabbitMQ 컨테이너 확인 및 제거
if [ "$(docker ps -aq -f name=rabbitmq)" ]; then
    echo "기존 RabbitMQ 컨테이너를 중지하고 제거합니다..."
    docker stop rabbitmq
    docker rm rabbitmq
fi

# RabbitMQ 실행
docker run -d \
    --name rabbitmq \
    -p 5672:5672 \
    -p 15672:15672 \
    -e RABBITMQ_DEFAULT_USER=guest \
    -e RABBITMQ_DEFAULT_PASS=guest \
    rabbitmq:3-management

echo "RabbitMQ가 시작되었습니다."
echo "관리자 UI: http://localhost:15672 (guest/guest)"