package com.ltj.myboard.dto.post;
import com.ltj.myboard.domain.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostAndFilesData {
    private Post postMasterData;
    private String[] postFileURLs;
}
