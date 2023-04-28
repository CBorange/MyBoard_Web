package com.ltj.myboard.repository;

import com.ltj.myboard.domain.UserGrade;

import java.util.List;
import java.util.Optional;

public interface UserGradeRepository {
    Optional<UserGrade> findGradeByLevel(int gradeLevel);
    Optional<UserGrade> findGradeByCaption(String caption);
}
