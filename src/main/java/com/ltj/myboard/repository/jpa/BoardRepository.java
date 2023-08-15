package com.ltj.myboard.repository.jpa;

import com.ltj.myboard.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Optional<Board> findBoardById(int id);
    List<Board> findAll();
    List<Board> findAllByParentBoardIsNull();

    @Query("SELECT b " +
            "FROM board b " +
            "WHERE 0 = (SELECT count(id) " +
            "           FROM board bb " +
            "           WHERE b.id = bb.parentBoard.id)")
    List<Board> findLeafBoards();
}
