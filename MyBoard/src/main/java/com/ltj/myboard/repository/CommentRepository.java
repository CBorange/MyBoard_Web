package com.ltj.myboard.repository;
import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.post.OrderedComment;

import java.util.List;

public interface CommentRepository {
    List<OrderedComment> findOrderedCommentByPostID(int postID);
    List<OrderedComment> findOrderedRootComment(int postID);
}
