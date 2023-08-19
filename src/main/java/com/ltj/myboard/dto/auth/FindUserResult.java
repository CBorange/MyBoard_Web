package com.ltj.myboard.dto.auth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FindUserResult {
    private String userId;
    private String password;
}
