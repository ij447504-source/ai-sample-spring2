# 🚩 작업 보고서: 회원가입 기능 고도화 (AOP & BCrypt)

- **작업 일시**: 2026-03-18
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)

```text
[사용자] --- (1. 가입 정보 입력) ---> [join-form.mustache]
                                          |
                                    (2. 중복 체크 / 주소 검색)
                                          |
[컨트롤러] <--- (3. POST /join) --- [가입하기 버튼]
    |
    +--- (4. ValidationAspect) : 유효성 검사 (AOP) --- [실패 시 alert]
    |
[서비스] --- (5. 비밀번호 암호화) ---> [BCrypt]
    |
[리포지토리] --- (6. DB 저장) ---> [H2 Database]
```

1. **의존성 설정**: AOP 및 Security Crypto 라이브러리 추가.
2. **공통 예외 처리**: `GlobalExceptionHandler`와 `Exception400`을 통해 브라우저 경고창(alert) 응답 구현.
3. **AOP 유효성 검사**: `ValidationAspect`를 구현하여 컨트롤러 진입 전 자동으로 입력값 검증.
4. **암호화 로직**: `BCryptPasswordEncoder` 빈 등록 및 서비스 계층에서 비밀번호 해싱 적용.
5. **UI/UX 고도화**: 디자인 시스템 적용 및 다음(Daum) 주소 API 연동.

## 2. 🧩 변경된 모든 코드 포함

### 1) GlobalExceptionHandler.java (공통 예외 처리기)
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception400.class)
    public String handleException400(Exception400 e) {
        // 에러 발생 시 사용자에게 친절한 경고창을 띄우고 이전 페이지로 돌려보냅니다.
        return """
                <script>
                    alert("%s");
                    history.back();
                </script>
                """.formatted(e.getMessage());
    }
}
```

### 2) ValidationAspect.java (AOP 유효성 검사)
```java
@Component
@Aspect
public class ValidationAspect {
    @Before("execution(* com.example.demo..*Controller.*(..)) && args(.., bindingResult)")
    public void validate(JoinPoint jp, BindingResult bindingResult) {
        // 컨트롤러의 메서드가 실행되기 직전에 가로채서 유효성 검사 오류가 있는지 확인합니다.
        if (bindingResult.hasErrors()) {
            var fieldError = bindingResult.getFieldErrors().get(0);
            throw new Exception400(fieldError.getDefaultMessage());
        }
    }
}
```

### 3) UserService.java (암호화 및 저장)
```java
@Transactional
public void join(UserRequest.Join reqDTO) {
    // 1. 아이디 중복 체크 (안전 장치)
    var userOp = userRepository.findByUsername(reqDTO.getUsername());
    if (userOp.isPresent()) throw new Exception400("이미 존재하는 아이디입니다.");

    // 2. 비밀번호 암호화 (BCrypt)
    var encPassword = passwordEncoder.encode(reqDTO.getPassword());
    reqDTO.setPassword(encPassword);

    // 3. DB 저장
    userRepository.save(reqDTO.toEntity());
}
```

## 3. 🍦 상세비유 쉬운 예시 (Easy Analogy)

"이번 작업은 **공항 보안 검색대**와 같습니다."

- **AOP 유효성 검사**는 비행기를 타기 전(컨트롤러 실행 전) 거치는 **보안 검색대**와 같습니다. 여권이 없거나 위험한 물건(잘못된 입력값)이 있으면 승무원(컨트롤러)을 만나기도 전에 입장이 거부됩니다.
- **BCrypt 암호화**는 중요한 서류를 **금고에 넣기 전 갈쇄(해싱)**하는 것과 같습니다. 누군가 금고를 열어보더라도 원래 어떤 내용이었는지 알 수 없게 만들어버리는 것이죠!

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **AOP (Aspect Oriented Programming)**: 핵심 로직(회원가입)과 공통 로직(유효성 검사)을 분리하는 기술입니다. 덕분에 컨트롤러 코드가 깔끔해집니다.
- **BCrypt Salting**: 같은 비밀번호라도 매번 다른 암호문이 생성되게 하는 기술입니다. 해커가 미리 계산해둔 표(Rainbow Table)를 쓸 수 없게 만듭니다.
- **Daum Postcode API**: 복잡한 주소 검색 기능을 외부 서비스를 통해 손쉽게 구현하여 사용자 편의성을 높였습니다.
