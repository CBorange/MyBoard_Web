package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "post")
@Getter
@Setter
@ToString(exclude = "comments")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="board_id")
    private int boardId;

    @Column(name="writer_id")
    private String writerId;

    @Column(name="writer_nickname")
    private String writerNickname;

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

    @OneToMany()
    @JoinColumn(name = "post_id")
    private List<Comment> comments;
}
