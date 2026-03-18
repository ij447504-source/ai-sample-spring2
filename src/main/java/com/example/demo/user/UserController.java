package com.example.demo.user;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid UserRequest.Join reqDTO, BindingResult bindingResult) {
        userService.join(reqDTO);
        return "redirect:/login-form"; // 로그인 페이지가 아직 없으므로 추후 구현 예정
    }

    // 회원가입 페이지 반환
    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }
}
