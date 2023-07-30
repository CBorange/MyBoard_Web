package com.ltj.myboard.dto.post;
import com.ltj.myboard.domain.PostScrap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderedPostScrap {
    private int orderedScrapNo;
    private PostScrap scrapData;
}
