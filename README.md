# 🎫 Real-time Seat Ticketing System
Spring Boot와 React를 활용한 대규모 좌석 실시간 예약 및 상태 관리 시스템 > 사용자의 예약 현황을 WebSocket을 통해 실시간으로 브로드캐스팅하며, 데이터 정합성을 보장합니다.

# 🚀 Key Features (주요 기능)
대규모 좌석 렌더링: Konva.js(Canvas API)를 활용하여 2,500개의 좌석을 끊김 없이 렌더링하고 줌/드래그 최적화.

실시간 상태 동기화: WebSocket(STOMP) 및 SockJS를 기반으로 좌석 예약 상태를 모든 접속자에게 0.1초 내외로 실시간 전파.

데이터 정합성 보장: 초기 로딩 시 **REST API(Snapshot)**로 상태를 동기화하고, 이후 변화는 **WebSocket(Delta)**으로 수신하는 하이브리드 방식 채택.

보안 및 접근 제어: Spring Security를 통한 CSRF 방어 및 도메인 기반 CORS 정책 수립.

# 🛠 Tech Stack (기술 스택)
Backend: Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security.

Frontend: React, TypeScript, Konva.js, Axios, StompJS.

Database: MySQL 8.0, Redis (캐싱 및 동시성 제어용).

Infrastructure: Oracle Cloud Infrastructure (OCI), Docker, Docker Compose.

# 🔥 Troubleshooting
### 1. 브라우저 보안 정책(CORS)과 인증 정보 충돌 해결
문제: allowCredentials(true) 설정 시 와일드카드(*) 사용 불가로 인한 400 Bad Request 발생.

해결: allowedOriginPatterns를 활용하여 요청 출처를 명시적으로 검증하고, Spring Security 필터 체인에 CORS 설정을 통합하여 해결.

### 2. 초기 로딩과 실시간 업데이트 간 데이터 정합성 확보
문제: 웹소켓 연결 전에 발생한 예약 내역이 초기 화면에 반영되지 않는 현상 발생.

해결: 컴포넌트 마운트 시점에 전체 예약 ID를 조회하는 API를 호출(Snapshot)하고, 이후의 변경 사항만 WebSocket으로 수신하는 구조를 설계하여 해결.

### 3. 클라우드 배포 시 빌드 캐시 문제 대응
문제: 소스 코드 수정 후 Docker 배포 시 이전 설정값이 유지되는 현상.

해결: Docker Compose 빌드 시 --no-cache 옵션을 사용하여 레이어를 재구성함으로써 클라우드 환경의 반영 신뢰성 확보.

# 💡 Insight
- 대형 시스템 운영 시 인프라(CORS, Port)와 애플리케이션 보안의 유기적인 연동 중요성
- 실시간성(WebSocket)을 확보하는 과정
