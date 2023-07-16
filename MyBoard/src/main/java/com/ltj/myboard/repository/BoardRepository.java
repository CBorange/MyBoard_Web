package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Optional<Board> findBoardById(int id);
    List<Board> findAll();
    List<Board> findAllByParentBoardIsNull();
}
