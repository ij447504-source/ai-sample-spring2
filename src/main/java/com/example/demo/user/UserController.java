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

    // 로그인 처리
    @PostMapping("/login")
    public String login(@Valid UserRequest.Login reqDTO, BindingResult bindingResult) {
        User sessionUser = userService.login(reqDTO);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    // 로그인 페이지 반환
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    // 회원 정보 수정 페이지 반환
    @GetMapping("/user/update-form")
    public String updateForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        return "user/update-form";
    }

    // 회원 정보 수정 처리
    @PostMapping("/user/update")
    public String update(@Valid UserRequest.Update reqDTO, BindingResult bindingResult) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        User newUser = userService.update(sessionUser.getId(), reqDTO);
        session.setAttribute("sessionUser", newUser);
        return "redirect:/";
    }

    // 회원 탈퇴 처리
    @PostMapping("/user/withdraw")
    public String withdraw() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        userService.withdraw(sessionUser.getId());
        session.invalidate();
        return "redirect:/";
    }
}
