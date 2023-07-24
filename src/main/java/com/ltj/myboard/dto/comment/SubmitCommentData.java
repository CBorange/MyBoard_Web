package com.ltj.myboard.dto.comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmitCommentData {
    private String postWriterId;
    private int postId;
    private Integer parentCommentId;
    private String writerId;
    private String writerNickname;
    private String content;
    private boolean isSubComment;
}
