package com.ltj.myboard.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class ChangeUserInfoRequest {
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Nickname is required")
    private String nickname;

    @NotEmpty(message = "Nickname is required")
    private String userID;

    @NotEmpty(message = "whether change password or not is required")
    private boolean changePassword;

    @NotEmpty(message = "Password is required")
    private String password;

    private String afterPassword;   // 비밀번호 변경 시 사용
}
