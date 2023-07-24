package com.ltj.myboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post_dislikes_history")
@Getter
@Setter
@ToString
public class PostDislikesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="post_id")
    private int postId;

    @Column(name="user_id")
    private String userId;

    @Column(name="created_day")
    private Date createdDay;

    @Column(name="modify_day")
    private Date modifyDay;
}
