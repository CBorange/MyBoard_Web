package com.ltj.myboard.repository.jpa;

import com.ltj.myboard.domain.PostScrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostScrapRepository extends JpaRepository<PostScrap, Integer> {
    Page<PostScrap> findAllByUserId(String userId, Pageable pageRequest);
    long countByPostIdAndUserId(int postId, String userId);
}
