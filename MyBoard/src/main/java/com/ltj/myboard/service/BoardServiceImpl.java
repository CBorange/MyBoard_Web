package com.ltj.myboard.service;
import com.ltj.myboard.domain.Board;
import org.springframework.stereotype.Service;
import java.util.Enumeration;

@Service
public class BoardServiceImpl implements BoardService {

    @Override
    public Board findBoardByID(int id) {
        return null;
    }

    @Override
    public Enumeration<Board> getAllBoards() {
        return null;
    }

    @Override
    public Enumeration<Board> getAllRootBoards() {
        return null;
    }
}
