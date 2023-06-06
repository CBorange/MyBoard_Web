package com.ltj.myboard.repository;
import com.ltj.myboard.domain.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findById(int commentID);
    List<Comment> findAllByPostIdAndParentCommentIsNull(int postId, Sort sort);
}
