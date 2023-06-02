package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "post")
@Getter
@Setter
@ToString
public class Post {
    public Post(){
        id = -1;    // ID의 기본값은 음수이다. 음수인 경우 신규 게시글로 취급한다.
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="board_id")
    private int boardId;

    @Column(name="writer_id")
    private String writerId;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name="view_count")
    private int viewCount;

    @Column(name="good_count")
    private int goodCount;

    @Column(name="bad_count")
    private int badCount;

    @Column(name="created_day")
    private Date createdDay;

    @Column(name="modify_day")
    private Date modifyDay;

    @Column(name="delete_day")
    private Date deleteDay;
}
