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

    // 로그인
    public User login(UserRequest.Login reqDTO) {
        // 아이디로 조회
        User user = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception400("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 비밀번호 비교 (BCrypt)
        if (!passwordEncoder.matches(reqDTO.getPassword(), user.getPassword())) {
            throw new Exception400("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    // 회원 정보 수정
    @Transactional
    public User update(Integer id, UserRequest.Update reqDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception400("회원정보를 찾을 수 없습니다."));

        // 비밀번호 암호화 후 반영
        String encPassword = passwordEncoder.encode(reqDTO.getPassword());
        user.setPassword(encPassword);
        user.setEmail(reqDTO.getEmail());

        return user;
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(Integer id) {
        // 실제로는 댓글 -> 게시글 -> 유저 순으로 삭제해야 하지만,
        // 여기서는 유저만 삭제 처리 (제약조건이 있는 경우 별도 처리 필요)
        userRepository.deleteById(id);
    }
}
