package com.ltj.myboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "comment_activity_history")
@Getter
@Setter
@ToString
public class CommentActivityHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="comment_id", nullable = false)
    private int commentId;

    @Column(name="user_id")
    private String userId;

    @Column(name="created_day")
    private Date createdDay;

    @Column(name="modify_day")
    private Date modifyDay;

    private int type;
}
