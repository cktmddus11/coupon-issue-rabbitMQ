# 빌드 단계
FROM gradle:jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY build.gradle settings.gradle ./
COPY coupon-api ./coupon-api
COPY coupon-core ./coupon-core
COPY coupon-web/build.gradle ./coupon-web/

# 의존성 및 애플리케이션 빌드
RUN gradle :coupon-api:bootJar --no-daemon

# 실행 단계
FROM eclipse-temurin:17-jre

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/coupon-api/build/libs/*.jar ./app.jar

# Spring Boot 애플리케이션 실행을 위한 환경 변수
ENV SPRING_PROFILES_ACTIVE=docker

# 컨테이너 포트 노출
EXPOSE 8080

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
