package com.ltj.myboard.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyInfo {
    private String userId;

    @Setter
    private String nickname;

    private String email;

    @Setter
    private String registerDay;

    private String loginDay;

    private long notificationCnt;

    private int grade;
}
