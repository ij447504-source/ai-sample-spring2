# 🚩 작업 보고서: 로그인/로그아웃 기능 구현

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
```text
[사용자] --(ID/PW 입력)--> [로그인 페이지] --(POST /login)--> [서버(Controller)]
                                                                  |
[홈페이지] <--(Redirect)-- [세션에 유저 저장] <--(User객체)-- [로그인 처리(Service)]
      |                                                           |
      +-- [헤더 내비게이션 바] (로그인 상태에 따라 메뉴 변경: 글쓰기/로그아웃 노출)
```
1. **Request DTO 설계**: 로그인 시 필요한 데이터를 담을 `UserRequest.Login` 클래스에 유효성 검사 추가.
2. **비즈니스 로직 구현**: `UserService`에서 DB의 암호화된 비밀번호와 입력된 비밀번호를 비교하는 로직 작성.
3. **컨트롤러 처리**: 로그인 성공 시 세션에 정보를 저장하고, 로그아웃 시 세션을 무효화하는 기능 구현.
4. **UI 구현**: Bootstrap을 활용한 로그인 폼 작성 및 헤더 메뉴의 동적 렌더링 적용.

## 2. 🧩 변경된 모든 코드 포함

### UserRequest.java (DTO)
```java
public class UserRequest {
    // ... 기존 코드
    @Data
    public static class Login {
        @NotBlank(message = "유저네임은 필수입니다")
        private String username;

        @NotBlank(message = "비밀번호는 필수입니다")
        private String password;
    }
}
```

### UserService.java (비즈니스 로직)
```java
public User login(UserRequest.Login reqDTO) {
    // 1. 아이디로 유저를 찾아요. 없으면 에러!
    User user = userRepository.findByUsername(reqDTO.getUsername())
            .orElseThrow(() -> new Exception400("아이디 또는 비밀번호가 일치하지 않습니다."));

    // 2. 입력한 비밀번호와 DB의 암호화된 비밀번호가 맞는지 확인해요.
    if (!passwordEncoder.matches(reqDTO.getPassword(), user.getPassword())) {
        throw new Exception400("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    return user;
}
```

### UserController.java (컨트롤러)
```java
@PostMapping("/login")
public String login(@Valid UserRequest.Login reqDTO, BindingResult bindingResult) {
    // 서비스에서 로그인 처리를 하고 유저 정보를 가져와요.
    User sessionUser = userService.login(reqDTO);
    // 가져온 유저 정보를 '세션'이라는 서버 저장소에 보관해요.
    session.setAttribute("sessionUser", sessionUser);
    return "redirect:/";
}

@GetMapping("/logout")
public String logout() {
    // 세션을 만료시켜서 로그아웃 상태로 만들어요.
    session.invalidate();
    return "redirect:/";
}
```

### header.mustache (UI 분기 처리)
```mustache
{{#sessionUser}} {{! 로그인했을 때만 보여요 }}
    <li class="nav-item"><a class="nav-link" href="/board/save-form">글쓰기</a></li>
    <li class="nav-item"><a class="nav-link text-danger" href="/logout">로그아웃</a></li>
{{/sessionUser}}
{{^sessionUser}} {{! 로그인 안 했을 때만 보여요 }}
    <li class="nav-item"><a class="nav-link" href="/join-form">회원가입</a></li>
    <li class="nav-item"><a class="nav-link btn btn-primary text-white" href="/login-form">로그인</a></li>
{{/sessionUser}}
```

## 3. 🍦 상세비유 쉬운 예시를 들어서 (Easy Analogy)
"이번 작업은 **도서관 회원증 발급 및 출입 관리**와 같습니다.
회원가입은 도서관에 내 정보를 등록하는 것이고, 로그인은 내 이름과 비밀번호를 말하고 **'오늘 하루 이용권(세션)'**을 받는 것과 비슷해요! 
로그아웃은 이 이용권을 반납하고 나가는 것이며, 내비게이션 바 메뉴가 바뀌는 것은 이용권이 있는 사람만 '도서 대여(글쓰기)' 구역에 들어갈 수 있게 문이 열리는 것과 같습니다."

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **BCrypt (passwordEncoder.matches)**: 비밀번호를 그대로 저장하면 위험하기 때문에 암호화해서 저장합니다. 로그인을 할 때는 사용자가 입력한 일반 텍스트 비밀번호와 DB에 있는 암호화된 비밀번호를 `matches` 함수로 비교하여 일치 여부를 확인합니다.
- **Session (HttpSession)**: 클라이언트(브라우저)와 서버 간의 연결 상태를 유지하기 위한 저장소입니다. 로그인이 성공하면 유저 객체를 세션에 담아두고, 이후 요청이 올 때마다 "아, 이 사람은 로그인한 사람이구나!"라고 서버가 인식하게 됩니다.
- **Mustache Conditional Rendering**: `{{#sessionUser}}` 문법을 사용하면 해당 객체가 존재할 때(로그인 상태)만 안쪽 코드를 화면에 그려줍니다. 반대로 `{{^sessionUser}}`는 없을 때(로그아웃 상태)만 그려줍니다.
