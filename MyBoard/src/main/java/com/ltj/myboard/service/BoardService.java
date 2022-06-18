package com.ltj.myboard.service;
import com.ltj.myboard.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    Optional<Board> findBoardByID(int id);
    List<Board> getAllBoards();
    List<Board> getAllRootBoards();
}
