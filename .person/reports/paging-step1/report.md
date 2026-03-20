# 🚩 작업 보고서: T-6.1 데이터 준비 및 기본 목록 보기

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
```text
[데이터 준비] (data.sql) -> [DTO 설계] (BoardResponse.Min) -> [비즈니스 로직] (BoardService) -> [컨트롤러 연결] (BoardController) -> [화면 구현] (list.mustache)
```
1. **데이터 준비**: 페이징 효과를 확인하기 위해 21개의 게시글 더미 데이터를 DB에 삽입했습니다.
2. **DTO 설계**: 화면 노출에 최적화된 `BoardResponse.Min` 클래스를 생성하여 엔티티를 보호했습니다.
3. **비즈니스 로직**: `게시글목록보기`라는 한글 메서드명을 사용하여 서비스 레이어의 가독성을 높였습니다.
4. **컨트롤러 연결**: 메인 페이지 요청 시 전체 목록을 조회하여 뷰로 전달하는 로직을 구현했습니다.
5. **화면 구현**: Bootstrap 5와 디자인 시스템을 적용하여 깔끔한 카드 스타일의 목록 화면을 제작했습니다.

## 2. 🧩 변경된 모든 코드 포함

### 1) DB 더미 데이터 추가 (`data.sql`)
```sql
-- 기존 3개에서 21개로 확장
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('첫 번째 게시글', '...', 1, NOW());
-- ... 중간 생략 ...
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('스물한 번째 게시글', '내용21', 2, NOW());
```

### 2) 목록 전용 DTO (`BoardResponse.java`)
```java
public class BoardResponse {
    @Data
    public static class Min { // 목록 조회를 위한 최소한의 데이터
        private Integer id;
        private String title;
        private String username;

        public Min(Board board) { // 엔티티를 DTO로 변환
            this.id = board.getId();
            this.title = board.getTitle();
            this.username = board.getUser().getUsername();
        }
    }
}
```

### 3) 서비스 로직 (`BoardService.java`)
```java
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.Min> 게시글목록보기() {
        // 1. 모든 게시글 조회
        List<Board> boards = boardRepository.findAll();
        // 2. 스트림을 사용하여 DTO 리스트로 변환
        return boards.stream()
                .map(BoardResponse.Min::new)
                .collect(Collectors.toList());
    }
}
```

### 4) 컨트롤러 (`BoardController.java`)
```java
@Controller
public class BoardController {
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        // 1. 서비스 호출
        List<BoardResponse.Min> boardList = boardService.게시글목록보기();
        // 2. 가방(request)에 담기
        request.setAttribute("boardList", boardList);
        // 3. 뷰 리턴
        return "board/list";
    }
}
```

### 5) Mustache 화면 (`list.mustache`)
```mustache
<table class="table table-hover">
    <tbody>
        {{#boardList}} {{! 데이터가 있을 때 반복 }}
        <tr>
            <td>{{id}}</td>
            <td><a href="/board/{{id}}">{{title}}</a></td>
            <td>{{username}}</td>
        </tr>
        {{/boardList}}
        {{^boardList}} {{! 데이터가 없을 때 실행 }}
        <tr><td colspan="3">게시글이 없습니다.</td></tr>
        {{/boardList}}
    </tbody>
</table>
```

## 3. 🍦 상세비유 쉬운 예시 (Easy Analogy)
이번 작업은 **'도서관에 책장과 도서 목록을 준비하는 것'**과 같습니다. 
- **DB 데이터**: 도서관에 새로 들어온 21권의 책입니다.
- **DTO**: 책 전체를 빌려주는 것이 아니라, 제목과 저자만 적힌 '대출 목록표'를 만드는 과정입니다.
- **Service/Controller**: 사서가 서고에서 책을 확인하고 대출 목록표를 작성하여 데스크에 올려두는 역할입니다.
- **Mustache**: 방문객이 데스크에서 깔끔하게 정리된 '대출 목록표'를 확인하는 화면입니다.

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **DTO (Data Transfer Object)**: 
  엔티티(`Board`)는 데이터베이스와 직접 연결된 매우 중요한 객체입니다. 이를 화면(View)까지 그대로 노출하면 보안상 위험하고, 화면에 필요 없는 정보(내용, 작성 시간 등)까지 포함되어 비효율적입니다. `BoardResponse.Min`과 같은 DTO를 사용하여 필요한 데이터만 딱 골라 전달하는 것이 핵심입니다.

- **Mustache 반복문과 조건문**:
  - `{{#boardList}}`: 리스트가 존재하면 그 안의 내용을 반복해서 출력합니다.
  - `{{^boardList}}`: 리스트가 비어있거나 null이면 실행되는 '반전' 조건문입니다. "데이터가 없습니다"와 같은 예외 처리를 UI에서 간단하게 할 수 있게 도와줍니다.

- **Stream API**: 
  `boards.stream().map(BoardResponse.Min::new).collect(Collectors.toList())` 코드는 리스트 내의 모든 엔티티를 하나씩 꺼내어 DTO 생성자에 집어넣고, 다시 리스트로 묶어주는 현대적인 Java 방식입니다. 코드가 간결해지고 가독성이 좋아집니다.
