package com.ltj.myboard.dto.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmitPostData {
    private String postName;
    private String content;
    private int boardID;
}
