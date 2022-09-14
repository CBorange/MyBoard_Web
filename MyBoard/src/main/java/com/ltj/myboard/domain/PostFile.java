package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostFile {
    private int ID;
    private int PostID;
    private String FileID;
    private String FileName;
}
