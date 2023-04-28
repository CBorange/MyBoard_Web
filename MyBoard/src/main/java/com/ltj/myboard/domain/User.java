package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "user")
@Getter
@Setter
public class User {
    @Id
    private String ID;

    private String Nickname;
    private String Password;

    @Column(name = "UserGrade")
    private int UserGrade;

    private String UserGradeName;
}
