package com.example.demo.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._core.handler.ex.Exception400;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 (비밀번호 암호화 후 저장)
    @Transactional
    public void join(UserRequest.Join reqDTO) {
        // 아이디 중복 체크
        var userOp = userRepository.findByUsername(reqDTO.getUsername());
        if (userOp.isPresent()) {
            throw new Exception400("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화 (BCrypt)
        var encPassword = passwordEncoder.encode(reqDTO.getPassword());
        reqDTO.setPassword(encPassword);

        // 엔티티 변환 후 저장
        userRepository.save(reqDTO.toEntity());
    }

    // 아이디 중복 체크 (true: 사용 가능, false: 중복)
    public boolean usernameSameCheck(String username) {
        var userOp = userRepository.findByUsername(username);
        return userOp.isEmpty();
    }
}
