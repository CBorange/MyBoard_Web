package com.ltj.myboard.domain;

import javax.persistence.AttributeConverter;

public enum BoardTypeDefiner {
    Best(1),
    Announcement(2),
    Common(3);

    private final int value;
    BoardTypeDefiner(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static BoardTypeDefiner fromInteger(int x){
        for (BoardTypeDefiner typeDefiner : BoardTypeDefiner.values()) {
            if (typeDefiner.getValue() == x) {
                return typeDefiner;
            }
        }
        throw new IllegalArgumentException("Invalid BoardTypeDefiner value: " + x);
    }
}


