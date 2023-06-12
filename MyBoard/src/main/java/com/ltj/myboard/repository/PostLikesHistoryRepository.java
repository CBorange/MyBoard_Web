package com.ltj.myboard.repository;

import com.ltj.myboard.domain.PostLikesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikesHistoryRepository extends JpaRepository<PostLikesHistory, Integer> {
    long countByPostIdAndUserId(int postId, String userId);
}
