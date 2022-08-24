package com.ltj.myboard.dto.comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmitCommentData {
    private int postID;
    private Integer parentCommentID;
    private String writerID;
    private String content;
}
