package com.ltj.myboard.repository;

import com.ltj.myboard.domain.PostDislikesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostDislikesHistoryRepository extends JpaRepository<PostDislikesHistory, Integer> {
    long countByPostId(int postId);
    long countByPostIdAndUserId(int postId, String userId);
    Optional<PostDislikesHistory> findByPostIdAndUserId(int postId, String userId);
}
