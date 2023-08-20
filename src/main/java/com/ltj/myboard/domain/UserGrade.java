package com.ltj.myboard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user_grade")
@Getter
public class UserGrade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Convert(converter = UserGradeLevelConverter.class)
    private UserGradeLevel grade;

    private String caption;

    private String icon;
}
