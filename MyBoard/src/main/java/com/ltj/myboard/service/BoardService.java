package com.ltj.myboard.service;
import com.ltj.myboard.domain.Board;

import java.util.Enumeration;

public interface BoardService {
    Board findBoardByID(int id);
    Enumeration<Board> getAllBoards();
    Enumeration<Board> getAllRootBoards();
}
