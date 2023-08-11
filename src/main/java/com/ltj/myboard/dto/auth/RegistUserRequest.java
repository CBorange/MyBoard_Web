package com.ltj.myboard.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class RegistUserRequest {
    @NotEmpty(message = "이메일은 필수값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
             message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotEmpty(message = "닉네임은 필수값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣])[a-zA-Z0-9가-힣]{2,12}$",
             message = "유효하지 않은 닉네임 형식 입니다.")
    private String nickname;

    @NotEmpty(message = "아이디는 필수값입니다.")
    @Pattern(regexp = "^(?=[a-zA-Z0-9]{4,12}$)(?=.*[a-zA-Z]).*$",
             message = "유효하지 않은 아이디 형식 입니다.")
    private String userID;

    @NotEmpty(message = "비밀번호는 필수값입니다.")
    @Pattern(regexp = "^(?=.*[A-Z!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$",
             message = "유효하지 않은 비밀번호 형식 입니다.")
    private String password;
}
