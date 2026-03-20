# 🚩 작업 보고서: 로그인 시스템 구현

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료
- **저장 경로**: `.person/reports/login-system/report.md`

## 1. 🌊 전체 작업 흐름 (Workflow)

### [ 로그인 페이지 구성 (UI Mockup) ]
```text
+---------------------------------------------------------+
| [Header Navbar]  홈  {{^sessionUser}}회원가입/로그인{{/sessionUser}}  |
|                      {{#sessionUser}}글쓰기/로그아웃{{/sessionUser}}  |
+---------------------------------------------------------+
|                                                         |
|   +-------------------------------------------------+   |
|   |                 [ 로그인 ]                      |   |
|   |-------------------------------------------------|   |
|   |                                                 |   |
|   |  아이디                                         |   |
|   |  [ input: "ssar" (username)               ]     |   |
|   |                                                 |   |
|   |  비밀번호                                       |   |
|   |  [ input: "1234" (password)               ]     |   |
|   |                                                 |   |
|   |         [       로그인하기 버튼       ]         |   |
|   |                                                 |   |
|   +-------------------------------------------------+   |
+---------------------------------------------------------+
```

1. **Request DTO 설계**: 로그인 시 필요한 데이터를 담을 `UserRequest.Login` 클래스에 유효성 검사 추가.
2. **비즈니스 로직 구현**: `UserService`에서 DB의 암호화된 비밀번호와 입력된 비밀번호를 `BCrypt`로 비교하는 로직 작성.
3. **데이터 초기화**: `data.sql`의 더미 비밀번호 `1234`를 `BCrypt` 해시값으로 업데이트하여 실제 로그인 가능하게 설정.
4. **컨트롤러 처리**: 로그인 성공 시 세션에 유저 정보를 저장하고, 로그아웃 시 세션을 무효화.
5. **UI 구현**: Bootstrap을 활용한 로그인 폼 작성 및 Mustache 조건부 렌더링으로 로그인 상태별 메뉴 분기 처리.

## 2. 🧩 핵심 코드 및 기술 딥다이브 (Technical Deep-dive)

### 🔐 비밀번호 검증 (BCrypt)
사용자가 입력한 `1234`와 DB의 `$2a$10$...` 해시값을 비교합니다.
```java
// UserService.java
public User login(UserRequest.Login reqDTO) {
    // 1. 유저 조회
    User user = userRepository.findByUsername(reqDTO.getUsername())
            .orElseThrow(() -> new Exception400("아이디 또는 비밀번호가 일치하지 않습니다."));

    // 2. 비밀번호 비교 (입력 PW vs DB 해시 PW)
    if (!passwordEncoder.matches(reqDTO.getPassword(), user.getPassword())) {
        throw new Exception400("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
    return user;
}
```

### 📦 세션 관리 (HttpSession)
로그인 성공 시 유저 정보를 서버 메모리에 보관하여 로그인 상태를 유지합니다.
```java
// UserController.java
@PostMapping("/login")
public String login(@Valid UserRequest.Login reqDTO, BindingResult bindingResult) {
    User sessionUser = userService.login(reqDTO);
    session.setAttribute("sessionUser", sessionUser); // 서버 세션에 저장
    return "redirect:/";
}
```

### 🎨 화면 분기 처리 (Mustache)
로그인 여부에 따라 헤더 메뉴를 다르게 보여줍니다.
```mustache
{{#sessionUser}} {{! 로그인했을 때 }}
    <li class="nav-item"><a class="nav-link" href="/board/save-form">글쓰기</a></li>
    <li class="nav-item"><a class="nav-link text-danger" href="/logout">로그아웃</a></li>
{{/sessionUser}}
{{^sessionUser}} {{! 로그인 안 했을 때 }}
    <li class="nav-item"><a class="nav-link btn btn-primary" href="/login-form">로그인</a></li>
{{/sessionUser}}
```

## 3. 🍦 상세비유 (Easy Analogy)
"이번 작업은 **도서관 회원증 발급 및 출입 관리**와 같습니다. 사용자는 **비밀번호 '1234'**라는 열쇠를 가지고 오고, 서버는 그 열쇠가 DB에 저장된 **'암호화된 자물쇠(해시값)'**에 맞는지 확인합니다. 열쇠가 맞으면 **'오늘 하루 출입권(세션)'**을 발급해주고, 이 출입권이 있는 사람만 도서관 안의 **'대출 구역(글쓰기)'**에 들어갈 수 있게 문을 열어주는 원리입니다!"
