package com.ltj.myboard.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class ChangeUserPasswordRequest {
    @NotEmpty(message = "유저ID는 필수값입니다.")
    private String userId;

    @NotEmpty(message = "현재 비밀번호는 필수값입니다.")
    private String curPassword;

    @NotEmpty(message = "변경 후 비밀번호가 비어있습니다.")
    @Pattern(regexp = "^^(?=.*[A-Z!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$",
            message = "변경 후 비밀번호가 유효하지 않은 형태입니다.")
    private String afterPassword;
}
