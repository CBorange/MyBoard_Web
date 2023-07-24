package com.ltj.myboard.util;
import lombok.Getter;
import lombok.Setter;

/*
* Pass by Reference 구현을 위한 Wrapper
*/
@Getter
@Setter
public class Ref<T> {
    private T value;
}
