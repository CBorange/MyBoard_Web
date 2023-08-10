package com.ltj.myboard.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Getter
@Setter
@ToString
public class ChangeUserInfoRequest {
    // 이메일 : 기본 이메일 구성(가운데 @ 기호, @ 우측에 .으로 시작하는 도메인)
    @NotEmpty(message = "이메일은 필수값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    // 닉네임 : 2~12자 이내, 영문, 숫자, 한글 사용가능
    @NotEmpty(message = "닉네임은 필수값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,12}$")
    private String nickname;

    @NotEmpty(message = "유저ID는 필수값입니다.")
    private String userID;

    @NotEmpty(message = "현재 비밀번호는 필수값입니다.")
    private String curPassword;
}
