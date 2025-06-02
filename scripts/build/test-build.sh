#!/bin/bash

# 설정 확인 및 테스트 수행
echo "모듈별 의존성 및 테스트 확인 중..."

# Core 모듈 빌드 및 테스트
echo "===== Core 모듈 빌드 및 테스트 ====="
./gradlew :coupon-core:build --stacktrace

# API 모듈 빌드 및 테스트
echo "===== API 모듈 빌드 및 테스트 ====="
./gradlew :coupon-api:build --stacktrace

echo "빌드 및 테스트 완료!"
