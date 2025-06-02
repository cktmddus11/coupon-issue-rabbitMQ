# RabbitMQ를 활용한 쿠폰 발급 시스템

RabbitMQ의 메시지 큐를 활용하여 비동기적으로 쿠폰을 발급하는 시스템입니다.

## 프로젝트 구조

이 프로젝트는 다음과 같은 멀티모듈 구조로 구성되어 있습니다.

- `coupon-core`: 공통 코어 모듈 (엔티티, 서비스 등)
- `coupon-api`: 백엔드 API 모듈
- `coupon-web`: 프론트엔드 모듈 (Next.js)

## 기술 스택

### 백엔드

- Java 17
- Spring Boot 3.1.x
- Spring AMQP (RabbitMQ)
- Spring Data JPA
- H2 Database

### 프론트엔드

- Next.js 15
- TypeScript
- Tailwind CSS

## Docker Compose로 실행하기

### 사전 준비

- Docker 및 Docker Compose 설치
- Git을 통해 프로젝트 복제

### Docker Compose 실행

```bash
# 스크립트로 실행
./start-docker.sh

# 또는 명령어로 직접 실행
docker-compose up -d
```

### Docker Compose 중지

```bash
# 스크립트로 실행
./stop-docker.sh

# 또는 명령어로 직접 실행
docker-compose down
```

### 서비스 접속 정보

- 프론트엔드: http://localhost:3000
- 백엔드 API: http://localhost:8080
- RabbitMQ 관리 콘솔: http://localhost:15672 (guest/guest)

## 수동으로 실행하기

### RabbitMQ 설치 및 실행

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

RabbitMQ 관리 콘솔: http://localhost:15672 (guest/guest)

### 백엔드 실행

```bash
# 루트 디렉토리에서
./gradlew coupon-api:bootRun
```

### 프론트엔드 실행

```bash
# coupon-web 디렉토리에서
npm install
npm run dev
```

## 주요 기능

1. 쿠폰 발급
   - REST API를 통한 쿠폰 발급 요청
   - RabbitMQ를 통한 비동기 처리
   - 응답 대기 및 결과 수신

2. 쿠폰 조회
   - 사용자별 쿠폰 목록 조회
   - 쿠폰 상태별 조회

## API 명세

### 쿠폰 발급 API

- **URL**: `/api/coupons/issue`
- **Method**: POST
- **Request**:
  ```json
  {
    "userId": "user123",
    "couponType": "DISCOUNT_AMOUNT",
    "discountAmount": 5000,
    "expiryDays": 30
  }
  ```
- **Response**:
  ```json
  {
    "id": 1,
    "code": "ABC123XYZ",
    "userId": "user123",
    "couponType": "DISCOUNT_AMOUNT",
    "discountAmount": 5000,
    "issuedAt": "2023-01-01T12:00:00",
    "expiresAt": "2023-01-31T12:00:00",
    "status": "ISSUED",
    "success": true,
    "message": "쿠폰이 성공적으로 발급되었습니다."
  }
  ```

### 사용자 쿠폰 목록 조회 API

- **URL**: `/api/coupons/user/{userId}`
- **Method**: GET
- **Response**: 쿠폰 목록 배열

## RabbitMQ 메시지 흐름

1. 클라이언트가 API 서버에 쿠폰 발급 요청
2. API 서버가 요청을 RabbitMQ 요청 큐로 전송
3. 워커가 요청 큐에서 메시지를 수신하여 쿠폰 발급 처리
4. 워커가 처리 결과를 RabbitMQ 응답 큐로 전송
5. API 서버가 응답 큐에서 메시지를 수신하여 클라이언트에게 결과 반환

## 프로젝트 개발 및 테스트

### 로컬 개발 환경

로컬에서 개발할 때는 먼저 RabbitMQ를 실행한 다음, 백엔드와 프론트엔드를 각각 실행하세요.

### 프로덕션 배포

Docker Compose를 사용하여 모든 서비스를 컨테이너화하여 배포합니다.
백엔드와 프론트엔드 모두 컨테이너 내에서 프로덕션 모드로 실행됩니다.

## 라이센스

MIT
