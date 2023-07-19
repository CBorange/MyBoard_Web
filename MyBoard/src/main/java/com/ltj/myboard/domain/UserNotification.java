package com.ltj.myboard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "user_notification")
@Getter
@Setter
public class UserNotification {
    @Id
    private int id;

    @Column(name="sender_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String senderId;

    @Column(name="user_id")
    private String userId;

    private String content;

    @Column(name = "\"read\"", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean read;

    @Column(name="created_day")
    private Date createdDay;

    @Column(name="modify_day")
    private Date modifyDay;

    @Column(name="delete_day")
    private Date deleteDay;
}
