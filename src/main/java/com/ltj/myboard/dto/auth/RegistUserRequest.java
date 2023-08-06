package com.ltj.myboard.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class RegistUserRequest {
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Nickname is required")
    private String nickname;

    @NotEmpty(message = "Nickname is required")
    private String userID;

    @NotEmpty(message = "Password is required")
    private String password;
}
