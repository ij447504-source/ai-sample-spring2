package com.example.demo._core.handler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.example.demo._core.handler.ex.Exception400;

@Component
@Aspect
public class ValidationAspect {

    // 모든 컨트롤러 메서드 실행 전 호출
    // args(.., bindingResult)를 통해 BindingResult 객체가 있는 메서드만 가로챔
    @Before("execution(* com.example.demo..*Controller.*(..)) && args(.., bindingResult)")
    public void validate(JoinPoint jp, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var fieldError = bindingResult.getFieldErrors().get(0);
            var message = fieldError.getDefaultMessage();
            throw new Exception400(message);
        }
    }
}
