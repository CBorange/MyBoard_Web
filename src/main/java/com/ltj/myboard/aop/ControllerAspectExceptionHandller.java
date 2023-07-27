package com.ltj.myboard.aop;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class ControllerAspectExceptionHandller {
    /**
     * NoSuchElementFoundException -> 404 not found, DB 조회결과 데이터 없을 때*/
    @ExceptionHandler
    protected ResponseEntity handleNoSuchElementFoundException(NoSuchElementException e){
        log.info("---------------------------------------------------------");
        log.info("API [404 Not Found] 오류 발생: " + e.getMessage());
        log.info("---------------------------------------------------------");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * IllegalArgumentException -> 400 Bad Request, @Valid, @Validated 등 Annotation이 아닌 Controller, Service 등
     * 직접 작성한 Validate 로직에서 유효하지 않은 Argument가 전송된 경우*/
    @ExceptionHandler
    protected ResponseEntity handleIllegalArgumentException(IllegalArgumentException e){
        log.info("---------------------------------------------------------");
        log.info("API [400 Bad Request] 오류 발생: " + e.getMessage());
        log.info("---------------------------------------------------------");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * MethodArgumentNotValidException -> @Valid 400 Bad Request*/
    @ExceptionHandler
    protected ResponseEntity handleMethodArgumentNotValidException (MethodArgumentNotValidException e){
        log.info("---------------------------------------------------------");
        log.info("API [400 Bad Request] 오류 발생: " + e.getMessage());
        log.info("---------------------------------------------------------");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * ConstraintViolationException -> @Validated 400 Bad Request*/
    @ExceptionHandler
    protected ResponseEntity handleConstraintViolationException (ConstraintViolationException e){
        log.info("---------------------------------------------------------");
        log.info("API [400 Bad Request] 오류 발생: " + e.getMessage());
        log.info("---------------------------------------------------------");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * IllegalStateException -> 500 Internal Server Error, 서버 동작 중 알려진 오류 발생한 경우*/
    @ExceptionHandler
    protected ResponseEntity handleIllegalStateException(IllegalStateException e){
        log.info("---------------------------------------------------------");
        log.info("API [500 Internal Server Error] IllegalStateException 오류 발생: " + e.getMessage());
        log.info("---------------------------------------------------------");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /**
     * Exception -> 500 internal server error, DB 조회 포함하여 서버 동작 중 알려지지 않은 오류 발생 시*/
    @ExceptionHandler
    protected ResponseEntity handleUnknownException(Exception e){
        // form Login에서 기본으로 제공하는 Authentication Filter 동작 시 아이디 or 비밀번호 일치하지 않는 경우 ->
        // DB에서 id에 해당하는 데이터를 찾지 못하거나 PasswordEncoder가 DB 데이터와 대조한 결과가 일치하지 않는 경우
        // 발생하는 Exception은 여기에 해당하지 않는다. ID 조회를 위한 UserService는 ID 존재하지 않는 경우
        // UsernameNotFoundException throw 하고 PasswordEncoder 대조 실패시에는 Filter 내부적으로 error 페이지로 돌려보냄

        log.info("---------------------------------------------------------");
        log.info("API [500 Internal Server Error] Unknown Exception 오류 발생: " + e.getMessage());
        log.info("---------------------------------------------------------");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
