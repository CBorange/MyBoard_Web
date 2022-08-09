package com.ltj.myboard.dto.post;

import com.ltj.myboard.domain.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 게시글의 댓글 목록 표시할 때 사용하는 dto Class
// 검색 순서 포함된 Comment 정보
// 원본 Comment domain(VO) Class는 Table column과 동일하게 field를 적용하기 위해서(추후 JPA 적용을 위해)
// 따로 dto Class 분리 함.
@Getter
@Setter
@ToString
public class OrderedComment {
    private int OrderedCommentNo;
    private Comment CommentData;
}
