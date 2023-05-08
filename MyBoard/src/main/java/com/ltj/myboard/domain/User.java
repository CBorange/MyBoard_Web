package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
}
