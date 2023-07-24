package com.ltj.myboard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "usergrade")
@Getter
@Setter
public class UserGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int grade;

    private String caption;

    private String icon;
}
