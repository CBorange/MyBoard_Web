package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Comment {
    private int ID;

    private int PostID;

    private int ParentCommentID;

    private String WriterID;

    private String Content;

    private int GoodCount;

    private int BadCount;

    private LocalDateTime CreatedDay;

    private LocalDateTime MoodifyDay;

    private LocalDateTime DeleteDay;
}
