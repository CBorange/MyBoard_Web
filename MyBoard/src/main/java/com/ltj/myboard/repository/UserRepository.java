package com.ltj.myboard.repository;

import com.ltj.myboard.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserByID(String ID);
    List<User> findUserByGrade(int grade);
}
