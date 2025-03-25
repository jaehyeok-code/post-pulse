# 실시간 인기 게시글을 제공하는 커뮤니티 플랫폼

## 프로젝트 소개
실시간 인기 게시글 및 다양한 기능을 제공하는 커뮤니티 플랫폼입니다. Kafka, Redis를 활용하여 실시간 데이터 처리를 수행하며, JWT 인증 및 OpenFeign을 통한 이메일 인증을 포함한 보안 기능을 제공합니다.

## 사용 기술
- **Backend**: Java 21, Spring Boot 3.4.4, Spring Security, JPA(복잡한쿼리문은 네이티브쿼리로 처리),
- **Database**: MySQL
- **Message Queue**: Kafka
- **Caching**: Redis
- **Storage**: AWS S3 (이미지 저장용)
- **Authentication**: JWT, Mailgun(OpenFeign)
- **API 문서화**: Swagger
- **Deployment**: Docker

## 구현 기능

### 회원 관리
- [ ] 회원가입 (이메일 인증 - Mailgun, OpenFeign 활용)
- [ ] 로그인 (JWT 인증 및 Spring Security 적용)
- [ ] 회원 정보 수정 및 탈퇴

### 게시글 관리
- [ ] 게시글 작성 / 수정 / 삭제
- [ ] 게시글 목록 조회 (최신순)
- [ ] 게시글 상세 조회

### 댓글 및 좋아요 기능
- [ ] 게시글에 대한 댓글 작성, 수정, 삭제
- [ ] 좋아요 등록 및 취소
- [ ] 댓글 및 좋아요 수 실시간 반영

### 알림 기능
 - [ ] 사용자의 게시글에 댓글이 달릴 경우 알림 발송
 - [ ] 사용자의 게시글에 좋아요가 눌릴 경우 알림 발송
 - [ ] Kafka를 활용하여 비동기 알림 이벤트 처리
 - [ ] Redis Pub/Sub을 이용한 실시간 알림 처리
 - [ ] 사용자가 알림 목록을 조회할 수 있도록 API 제공

### 게시글 검색 기능
- [ ] 게시글 제목 또는 사용자가 입력한 부분 문자열로 제목에 포함된 게시글을 검색
- [ ] 검색 결과가 많을 경우 페이징 처리하여 사용자에게 제공

### 실시간 인기 게시글 (Kafka + Redis 활용)
- [ ] 게시글 인기 점수 계산 (조회수 = 1점, 좋아요 = 5점, 댓글 = 3점), (동일 유저가 중복 조회해도 조회수는 증가하지 않도록 처리)
- [ ] Kafka를 활용하여 조회 이벤트 수집
- [ ] Redis SortedSet을 이용한 실시간 인기 게시글 반영
- [ ] 인기 게시글 피드 제공  
