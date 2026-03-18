package com.example.demo._core.handler.ex;

import lombok.Getter;

// HTTP 400 Bad Request 상황에서 사용할 커스텀 예외 클래스
@Getter
public class Exception400 extends RuntimeException {

    public Exception400(String message) {
        super(message);
    }
}
