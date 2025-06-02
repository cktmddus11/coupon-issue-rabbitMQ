#!/bin/bash

# Gradle 빌드 캐시와 폴더 삭제
echo "Gradle 빌드 캐시와 폴더 삭제 중..."
./gradlew clean

# .gradle 폴더 삭제
rm -rf .gradle

# build 폴더 삭제
rm -rf build
rm -rf */build

# IDE 관련 파일 리프레시
echo "IDE 설정 파일 재생성 중..."
./gradlew idea --refresh-dependencies

echo "완료! 이제 프로젝트를 다시 빌드하세요."
