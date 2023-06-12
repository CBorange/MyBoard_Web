package com.ltj.myboard.repository;

import com.ltj.myboard.domain.PostDislikesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDislikesHistoryRepository extends JpaRepository<PostDislikesHistory, Integer> {
    long countByPostIdAndUserId(int postId, String userId);
}
