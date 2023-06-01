package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "user")
@Getter
@Setter
public class User {
    @Id
    private String id;

    private String nickname;
    private String password;

    @ManyToOne()
    @JoinColumn(name="grade_id", nullable = false)
    private UserGrade userGrade;

    @Column(name="register_day")
    private Date registerDay;

    @Column(name="login_day")
    private Date loginDay;

    private String email;
}
