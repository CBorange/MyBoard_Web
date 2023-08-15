package com.ltj.myboard.repository.jpa;

import com.ltj.myboard.domain.User;
import com.ltj.myboard.domain.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserGradeRepository extends JpaRepository<UserGrade, String> {
    Optional<UserGrade> findByGrade(int grade);
    Optional<UserGrade> findByCaption(String caption);
}
