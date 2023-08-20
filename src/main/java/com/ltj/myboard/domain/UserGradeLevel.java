package com.ltj.myboard.domain;

public enum UserGradeLevel {
    Admin(1 << 0),  // 01
    User(1 << 1);   // 10

    private final int value;
    UserGradeLevel(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static UserGradeLevel fromInteger(int x){
        for (UserGradeLevel gradeLevel : UserGradeLevel.values()) {
            if (gradeLevel.getValue() == x) {
                return gradeLevel;
            }
        }
        throw new IllegalArgumentException("Invalid UserGradeLevel value: " + x);
    }
}
