package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Post {
    private int ID;

    private int BoardID;

    private String WriterID;

    private String Title;

    private String Content;

    private int ViewCount;

    private int GoodCount;

    private int BadCount;

    private LocalDateTime CreatedDay;

    private LocalDateTime ModifyDay;

    private LocalDateTime DeleteDay;
}
