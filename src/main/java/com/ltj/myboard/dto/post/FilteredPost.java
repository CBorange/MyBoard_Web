package com.ltj.myboard.dto.post;

import com.ltj.myboard.domain.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 게시글 목록 표시할 때 사용하는 dto Class
// 검색 조건(제목, 내용, 댓글 등)및
// 정렬 메소드(일자, 추천수, 제목 등)에 따라 정렬된 Post의 정보를 담는다.
@Getter
@Setter
@ToString
public class FilteredPost {
    private int OrderedPostNo;
    private long CommentCount;
    private long likeCount;
    private Post PostData;
}
