package com.ltj.myboard.dto.post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
* 추천/비추천 실행 Request DTO
* */
@Getter
@Setter
@ToString
public class ApplyLikeData {
    private int postId;
    private String userId;
}
