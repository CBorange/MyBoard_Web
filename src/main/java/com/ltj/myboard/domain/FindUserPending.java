package com.ltj.myboard.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

// 레디스 DB 저장용 Domain Class
// 유저 ID/PW 찾기 처리대기 데이터
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "find_user_pending", timeToLive = 300)   // 유효시간 5분
public class FindUserPending {

    @Id
    @Indexed
    private String uniqueLinkParam;

    private String userId;
}
