package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostFile {
    private int id;
    private int postId;
    private String fileId;
    private String fileName;
}
