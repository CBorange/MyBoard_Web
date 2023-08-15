package com.ltj.myboard.repository.jpa;

import com.ltj.myboard.domain.PostActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PostActivityHistoryRepository extends JpaRepository<PostActivityHistory, Integer> {
    long countByTypeAndPostIdAndUserId(int type, int postId, String userId);
    List<PostActivityHistory> findAllByTypeAndPostId(int type, int postId);
    Optional<PostActivityHistory> findByTypeAndPostIdAndUserId(int type, int postId, String userId);
}
