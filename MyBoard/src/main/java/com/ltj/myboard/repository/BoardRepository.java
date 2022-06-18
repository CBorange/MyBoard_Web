package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Optional<Board> findBoardByID(int id);
    List<Board> getAllBoards();
    List<Board> getAllRootBoards();
}
