package com.ltj.myboard.repository;

import com.ltj.myboard.domain.PostLikesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikesHistoryRepository extends JpaRepository<PostLikesHistory, Integer> {
    long countByPostId(int postId);
    long countByPostIdAndUserId(int postId, String userId);
    Optional<PostLikesHistory> findByPostIdAndUserId(int postId, String userId);
}
