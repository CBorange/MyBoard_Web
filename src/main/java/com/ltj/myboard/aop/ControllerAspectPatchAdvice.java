package com.ltj.myboard.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerAspectPatchAdvice {
    /**
     * PatchMapping API 호출 시점 before Advice*/
    @Before(value = "execution(@org.springframework.web.bind.annotation.PatchMapping * com.ltj.myboard.controller..*.*(..))")
    public void beforeAdvice(JoinPoint joinPoint){
        String msg = String.format("PATCH API Called: %s", joinPoint.getSignature());
        log.info(msg);
    }

    /**
     * PatchMapping API 호출 후 정상적으로 결과가 Return 되었을 때 Advice*/
    @AfterReturning(value = "execution(@org.springframework.web.bind.annotation.PatchMapping * com.ltj.myboard.controller..*.*(..))")
    public void afterRerturningAdvice(JoinPoint joinPoint){
        String msg = String.format("PATCH API Successfully returned : %s", joinPoint.getSignature());
        log.info(msg);
    }
}