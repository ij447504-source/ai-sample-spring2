package com.example.demo.board;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final HttpSession session;

    @GetMapping({"/", "/board/list"})
    public String home(HttpServletRequest request) {
        List<BoardResponse.Min> boardList = boardService.게시글목록보기();
        request.setAttribute("boardList", boardList);
        return "board/list";
    }
}
