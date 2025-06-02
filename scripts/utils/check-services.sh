#!/bin/bash

echo "===== 서비스 상태 확인 ====="

# RabbitMQ 확인
echo -n "RabbitMQ (5672): "
if nc -z localhost 5672 2>/dev/null; then
    echo "✅ 실행 중"
else
    echo "❌ 중지됨"
fi

echo -n "RabbitMQ 관리 UI (15672): "
if nc -z localhost 15672 2>/dev/null; then
    echo "✅ 실행 중"
else
    echo "❌ 중지됨"
fi

# 백엔드 API 확인
echo -n "백엔드 API (8080): "
if nc -z localhost 8080 2>/dev/null; then
    echo "✅ 실행 중"
else
    echo "❌ 중지됨"
fi

# 프론트엔드 확인
echo -n "프론트엔드 (3000): "
if nc -z localhost 3000 2>/dev/null; then
    echo "✅ 실행 중"
else
    echo "❌ 중지됨"
fi

echo ""
echo "===== 접속 URL ====="
echo "RabbitMQ 관리 UI: http://localhost:15672"
echo "백엔드 API: http://localhost:8080"
echo "Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo "H2 Console: http://localhost:8080/h2-console"
echo "프론트엔드: http://localhost:3000"