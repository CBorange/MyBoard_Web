package com.ltj.myboard.model;

public enum UserGradeLevel {
    Admin(1),
    User(2);

    private final int value;
    UserGradeLevel(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
