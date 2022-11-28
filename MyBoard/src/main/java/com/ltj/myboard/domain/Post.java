package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Post {
    public Post(){
        ID = -1;    // ID의 기본값은 음수이다. 음수인 경우 신규 게시글로 취급한다.
    }
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
