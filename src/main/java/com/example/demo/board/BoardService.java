package com.example.demo.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * DTO는 Service에서 만든다. Entity를 Controller에 전달하지 않는다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponse.Min> 게시글목록보기(int page) {
        int limit = 3; // 한 페이지에 보여줄 개수
        int offset = page * limit; // 시작인덱스
        List<Board> boards = boardRepository.mFindAll(limit, offset);
        return boards.stream()
                .map(BoardResponse.Min::new)
                .collect(Collectors.toList());
    }
}
