package com.ltj.myboard.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthDTO {
    private String nickname;
    private String userID;
    private String password;
    private String afterPassword;   // 비밀번호 변경 시 사용
}
