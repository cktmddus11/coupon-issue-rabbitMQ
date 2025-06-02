#!/bin/bash

# 현재 스크립트 위치에서 프로젝트 루트로 이동
cd "$(dirname "$0")/../.."

echo "===== Gradle 의존성 새로고침 ====="
./gradlew --refresh-dependencies

echo "===== IDE 설정 파일 재생성 ====="
./gradlew idea --refresh-dependencies

echo "완료!"