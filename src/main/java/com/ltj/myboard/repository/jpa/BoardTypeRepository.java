package com.ltj.myboard.repository.jpa;

import com.ltj.myboard.domain.BoardType;
import com.ltj.myboard.domain.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardTypeRepository extends JpaRepository<BoardType, Integer> {
    Optional<UserGrade> findByType(int type);
    Optional<UserGrade> findByName(String name);
}
