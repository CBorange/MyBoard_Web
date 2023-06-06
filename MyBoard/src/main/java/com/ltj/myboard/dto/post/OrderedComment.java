package com.ltj.myboard.dto.post;

import com.ltj.myboard.domain.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 게시글의 댓글 목록 표시할 때 사용하는 dto Class
// 표출 순서 포함된 Comment 정보
// 댓글 표출순서는 댓글 작성일자를 기준으로 정렬돼서 출력됨
@Getter
@Setter
@ToString
public class OrderedComment {
    private int OrderedCommentNo;
    private Comment CommentData;
}
