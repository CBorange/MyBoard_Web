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

    private int board_id;

    private String writer_id;

    private String title;

    private String content;

    private int view_count;

    private int good_count;

    private int bad_count;

    private LocalDateTime created_day;

    private LocalDateTime modify_day;

    private LocalDateTime delete_day;
}
