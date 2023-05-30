package com.ltj.myboard.repository;

import com.ltj.myboard.domain.UserGrade;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGradeRepository {
    Optional<UserGrade> findByGrade(int grade);
    Optional<UserGrade> findByCaption(String caption);
}
