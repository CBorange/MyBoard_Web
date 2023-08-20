package com.ltj.myboard.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyInfo {
    private String userId;
    private String nickname;
    private String email;
    private String registerDay;
    private String loginDay;
    private int grade;
}
