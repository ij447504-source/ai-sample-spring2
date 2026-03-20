# 상세 개발 태스크 (Detailed Tasks)

## Phase 1: 기반 구조 및 핵심 도메인 설계 (Infrastructure & Core Domain)

- [x] **T-1.1 프로젝트 초기 설정**
  - [x] Spring Boot 3.x, Java 21 설정 확인
  - [x] Dependencies 설정 (Spring Data JPA, H2, Mustache, Validation, Lombok)
- [x] **T-1.2 데이터베이스 스키마 설계 및 엔티티 구현**
  - [x] `User` 엔티티 구현 (id, username, password, email, address 등)
  - [x] `Board` 엔티티 구현 (id, title, content, user, createdAt)
  - [x] `Reply` 엔티티 구현 (id, comment, user, board, createdAt)
- [x] **T-1.3 공통 응답 규격 및 유틸리티 설정**
  - [x] `Resp<T>` 클래스 설계 및 구현 (공통 응답 DTO)
  - [x] 기본 예외 처리 구조 기초 설계
- [x] **T-1.4 H2 및 JPA 환경 설정**
  - [x] `application.properties` 설정 (H2 Console, Hibernate Logging)
  - [x] 초기 데이터 구성을 위한 `data.sql` 작성
- [x] **T-1.5 공통 화면 레이아웃 구성**
  - [x] `frontend-design` 스킬을 활용하여 `header.mustache`, `footer.mustache` 파일 작성 및 공통화 적용
  - [x] Bootstrap 등을 활용한 기본 레이아웃 템플릿 구조화

## Phase 2: 회원 인증 시스템 (Membership System)

- [ ] **T-2.1 회원가입 기능 및 화면 구현**
  - [ ] `frontend-design` 스킬을 활용한 회원가입 폼 (`join-form.mustache`) 구현
  - [ ] `UserRequest.JoinDTO` 구현 및 유효성 검사 적용
  - [ ] 아이디 중복 체크 API 구현 및 서비스 로직 작성
  - [ ] 비밀번호 암호화 저장 처리 (BCrypt 적용 권장)
- [x] **T-2.2 로그인/로그아웃 기능 및 화면 구현**
  - [x] `frontend-design` 스킬을 활용한 로그인 폼 (`login-form.mustache`) 구현
  - [x] `UserRequest.LoginDTO` 구현
  - [x] 세션(`HttpSession`)을 이용한 인증 정보 관리
  - [x] 로그인 성공/실패 시나리오 대응 및 리다이렉션 처리
- [x] **T-2.3 회원 탈퇴 및 마이페이지 화면 구현**
  - [x] `frontend-design` 스킬을 활용한 회원 정보 수정 화면 (`update-form.mustache`) 구현
  - [x] 회원 탈퇴 API 구현
  - [x] 탈퇴 시 해당 사용자가 작성한 게시글 및 댓글 일괄 처리 (Soft delete 또는 Cascade delete 전략 결정)
- [ ] **T-2.4 권한 관리 및 보안**
  - [ ] 인증이 필요한 페이지 접근 제어를 위한 Interceptor 구현
  - [ ] 세션 체크 로직 공통화

## Phase 3: 게시글 관리 시스템 (Core Blog Features)

- [ ] **T-3.1 게시글 CRUD 기능 및 화면 구현**
  - [ ] `frontend-design` 스킬을 활용한 게시글 목록 화면 (`list.mustache`), 상세 화면 (`detail.mustache`), 작성 화면 (`save-form.mustache`), 수정 화면 (`update-form.mustache`) 구현
  - [ ] 게시글 목록 조회 (`BoardRepository.findAll`)
  - [ ] 게시글 상세 보기 (작성자 정보 및 댓글 포함)
  - [ ] 게시글 작성/수정/삭제 API 및 서비스 로직
- [ ] **T-3.2 페이징 처리 (Pageable) 적용**
  - [ ] 목록 조회 시 `Pageable` 파라미터 적용
  - [ ] `frontend-design` 스킬을 활용하여 페이징 내비게이션 UI 구현
- [ ] **T-3.3 게시글 검색 기능 구현**
  - [ ] 제목 또는 내용 키워드 검색 기능 (`Containing` 쿼리 메서드 활용)
  - [ ] 검색 결과 페이징 연동 및 검색 폼 UI 구현
- [ ] **T-3.4 게시글 상세 정보 연동**
  - [ ] 상세 페이지에서 작성자 프로필 정보 노출

## Phase 4: 댓글 및 상호작용 (Interaction Features)

- [ ] **T-4.1 댓글 작성 기능 및 화면 연동**
  - [ ] `frontend-design` 스킬을 활용한 댓글 입력 폼 및 댓글 목록 UI (`detail.mustache` 내) 구현
  - [ ] `ReplyRequest.SaveDTO` 구현
  - [ ] AJAX 또는 Form 방식을 이용한 댓글 등록 로직
- [ ] **T-4.2 댓글 삭제 구현**
  - [ ] 댓글 삭제 API 구현 및 작성자 본인 확인 로직 적용
  - [ ] 비동기 삭제 처리 시 UI 업데이트 로직
- [ ] **T-4.3 게시글 상세 페이지 통합**
  - [ ] 게시글 조회 시 댓글 리스트 Fetch Join 또는 별도 조회 최적화
  - [ ] 댓글 개수 표시 기능 추가

## Phase 5: 예외 처리 및 안정화 (Polishing & Validation)

- [ ] **T-5.1 글로벌 예외 처리 (GlobalExceptionHandler)**
  - [ ] `@ControllerAdvice`를 이용한 공통 에러 페이지 처리
  - [ ] REST API 에러 응답(`Resp.fail`) 처리 로직 강화
- [ ] **T-5.2 데이터 유효성 검사 (Bean Validation)**
  - [ ] 모든 Request DTO에 `@NotBlank`, `@Size` 등 제약 조건 적용
  - [ ] BindingResult를 이용한 에러 메시지 사용자 노출
- [ ] **T-5.3 통합 테스트 및 통합 검수**
  - [ ] 주요 시나리오(회원가입 -> 글쓰기 -> 댓글 -> 탈퇴) 테스트
  - [ ] 성능 병목 구간(N+1 문제 등) 확인 및 해결

## Phase 6: 페이징 학습 로드맵 (Paging Learning Roadmap)

- [x] **T-6.1 데이터 준비 및 기본 목록 보기**
  - [x] `data.sql`에 충분한 양의 게시글 더미 데이터(약 20개 이상) 추가
  - [x] `BoardRepository.findAll()`을 사용하여 전체 목록을 `list.mustache`에 출력
  - [x] CSS 디자인 시스템을 적용하여 기본 목록 레이아웃 완성
- [x] **T-6.2 SQL 기초 페이징 (Repository 직접 구현)**
  - [x] H2의 `LIMIT`, `OFFSET`을 사용하는 네이티브 쿼리 작성 (`BoardRepository`)
  - [x] `page` 파라미터를 컨트롤러에서 받아 동적 쿼리 호출 (0페이지부터 시작)
  - [x] 서비스 레이어에서 비즈니스 로직(한글 메서드명) 구현
- [ ] **T-6.3 UI 제어 : 이전/다음 버튼 구현**
  - [ ] 현재 페이지가 첫 번째인지(`isFirst`), 마지막인지(`isLast`) 판별하는 로직 추가
  - [ ] Mustache의 `{{#isFirst}}` 등을 활용해 버튼 활성/비활성화 처리
  - [ ] 이전/다음 클릭 시 `?page=n` 형태의 쿼리 스트링 이동 처리
- [ ] **T-6.4 전체 페이지 계산 및 번호 바(Bar) 출력**
  - [ ] `SELECT COUNT(*)` 쿼리를 통해 전체 데이터 개수 파악
  - [ ] 전체 페이지 수(`totalPages`) 계산 로직 구현
  - [ ] 페이지 번호 리스트(`1, 2, 3...`)를 생성하여 화면에 반복문으로 출력
- [ ] **T-6.5 완성도 높이기 및 예외 처리**
  - [ ] 페이지 블록 단위 처리 (예: 5개씩 끊어서 보기)
  - [ ] 범위를 벗어난 페이지 요청 시 적절한 에러 응답 또는 0페이지로 리다이렉트
  - [ ] 현재 선택된 페이지 번호 강조 스타일 적용
