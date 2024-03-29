package com.ltj.myboard.repository.jpa;
import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    long countByPostId(int postId);
    Optional<Comment> findById(int commentID);
    Page<Comment> findAllByPostIdAndParentCommentIsNull(int postId, Pageable pageable);
    List<Comment> findAllByPostIdAndParentCommentIsNull(int postId);

    Page<Comment> findAllByWriterId(String writerId, Pageable pageable);
}
