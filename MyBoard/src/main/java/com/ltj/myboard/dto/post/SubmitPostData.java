package com.ltj.myboard.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmitPostData {
    private String title;
    private String content;
    private String[] imageSource;
    private int boardID;
    private String writerID;
}
