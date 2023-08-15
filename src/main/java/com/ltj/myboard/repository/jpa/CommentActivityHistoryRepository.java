package com.ltj.myboard.repository.jpa;
import com.ltj.myboard.domain.CommentActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CommentActivityHistoryRepository extends JpaRepository<CommentActivityHistory, Integer> {
    long countByTypeAndCommentIdAndUserId(int type, int commentId, String userId);
    List<CommentActivityHistory> findAllByTypeAndCommentId(int type, int commentId);
    Optional<CommentActivityHistory> findByTypeAndCommentIdAndUserId(int type, int commentId, String userId);
}
