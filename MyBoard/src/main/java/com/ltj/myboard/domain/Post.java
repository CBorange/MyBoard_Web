package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Post {
    public Post(){
        id = -1;    // ID의 기본값은 음수이다. 음수인 경우 신규 게시글로 취급한다.
    }
    private int id;

    private int boardId;

    private String writerId;

    private String title;

    private String content;

    private int viewCount;

    private int goodCount;

    private int badCount;

    private LocalDateTime createdDay;

    private LocalDateTime modifyDay;

    private LocalDateTime deleteDay;
}
