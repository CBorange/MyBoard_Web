package com.ltj.myboard.dto.ftp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserImageURL {
    private String url;
    private String uploadedFileID;
    private String uploadedFileName;
}
