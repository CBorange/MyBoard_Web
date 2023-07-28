package com.ltj.myboard.model;

/**
 * Post, Comment 등의 활동기록 데이터 타입 enum*/
public enum ActivityHistoryTypes {
    View(1),
    Like(2),
    Dislike(3);

    private final int value;
    ActivityHistoryTypes(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
