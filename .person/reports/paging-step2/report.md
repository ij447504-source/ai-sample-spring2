# 🚩 작업 보고서: T-6.2 SQL 기초 페이징 (Repository 직접 구현)

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
```text
[JPQL 작성] (mFindAll) -> [Offset 계산] (Service) -> [파라미터 수신] (Controller) -> [데이터 변화 확인] (Browser)
```
1. **JPQL 작성**: 최신 하이버네이트 문법을 활용하여 `LIMIT`와 `OFFSET`이 포함된 `mFindAll` 쿼리를 작성했습니다.
2. **Offset 계산**: 서비스 레이어에서 `page * limit` 수식을 통해 조회 시작 지점을 동적으로 계산했습니다.
3. **파라미터 수신**: 컨트롤러에서 `@RequestParam`을 통해 페이지 번호를 수신하고, 기본값(0)을 설정했습니다.
4. **데이터 변화 확인**: 브라우저 주소창에 `?page=1` 등을 입력하여 3개씩 데이터가 교체되는 것을 확인했습니다.

## 2. 🧩 변경된 모든 코드 포함

### 1) Repository 커스텀 쿼리 (`BoardRepository.java`)
```java
public interface BoardRepository extends JpaRepository<Board, Integer> {
    // JPQL에서도 LIMIT/OFFSET 사용 가능 (Hibernate 6.0+)
    @Query("SELECT b FROM Board b ORDER BY b.id DESC LIMIT :limit OFFSET :offset")
    List<Board> mFindAll(@Param("limit") int limit, @Param("offset") int offset);
}
```

### 2) 서비스 레이어 페이징 로직 (`BoardService.java`)
```java
public List<BoardResponse.Min> 게시글목록보기(int page) {
    int limit = 3; // 한 페이지에 보여줄 개수
    int offset = page * limit; // 시작 지점 계산 (0, 3, 6, ...)
    
    List<Board> boards = boardRepository.mFindAll(limit, offset);
    return boards.stream()
            .map(BoardResponse.Min::new)
            .collect(Collectors.toList());
}
```

### 3) 컨트롤러 파라미터 처리 (`BoardController.java`)
```java
@GetMapping({"/", "/board/list"})
public String home(@RequestParam(name = "page", defaultValue = "0") int page, HttpServletRequest request) {
    // 페이지 번호를 받아 서비스로 전달
    List<BoardResponse.Min> boardList = boardService.게시글목록보기(page);
    request.setAttribute("boardList", boardList);
    return "board/list";
}
```

## 3. 🍦 상세비유 쉬운 예시 (Easy Analogy)
이번 작업은 **'두꺼운 책에서 내가 보고 싶은 쪽수(Page)를 찾아 펼치는 것'**과 같습니다.
- **LIMIT (3)**: 한 번에 눈에 들어오는 '줄 수'입니다. (한 번에 3줄씩 읽겠다!)
- **OFFSET (page * 3)**: 내가 읽기 위해 건너뛰어야 할 '줄 수'입니다.
  - 0페이지: 0줄 건너뛰고 1, 2, 3줄 읽기
  - 1페이지: 3줄 건너뛰고 4, 5, 6줄 읽기
  - 2페이지: 6줄 건너뛰고 7, 8, 9줄 읽기

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

- **JPQL의 LIMIT/OFFSET**: 
  과거 JPA 표준에서는 `LIMIT`와 `OFFSET`을 지원하지 않아 `setFirstResult`, `setMaxResults` 메서드를 사용해야 했습니다. 하지만 하이버네이트 6.0부터는 JPQL 쿼리문 안에 직접 이 키워드들을 넣을 수 있게 되어 SQL과 매우 유사한 직관적인 쿼리 작성이 가능해졌습니다.

- **Zero-based Index**: 
  프로그래밍 세계와 DB의 OFFSET은 보통 0부터 시작합니다. 따라서 사용자가 '1페이지'를 보고 싶어 한다면, 내부적으로는 `page=0`으로 처리하거나, 연산 시 `-1`을 해주는 등의 보정이 필요할 수 있습니다. 이번 학습에서는 가장 직관적인 **0페이지 방식**을 선택했습니다.
