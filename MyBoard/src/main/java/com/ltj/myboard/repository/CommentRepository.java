package com.ltj.myboard.repository;
import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.post.OrderedComment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<OrderedComment> findOrderedCommentByPostID(int postID);
    List<OrderedComment> findOrderedRootComment(int postID);
    Optional<Comment> findCommentByID(int commentID);
    int insertComment(int postID, Integer parentCommentID, String writerID, String content);
}
