package com.ltj.myboard.repository;

import com.ltj.myboard.domain.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    Page<UserNotification> findAllByUserId(String userId, Pageable pageable);
}