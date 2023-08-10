package com.ltj.myboard.dto.admin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminChangePassword {
    private String userId;
    private String password;
}
