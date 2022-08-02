package com.ltj.myboard.repository;
import com.ltj.myboard.domain.Comment;
import java.util.List;

public interface CommentRepository {
    List<Comment> findCommentByPostID(int postID);
    List<Comment> findAllRootComment(int postID);
}
