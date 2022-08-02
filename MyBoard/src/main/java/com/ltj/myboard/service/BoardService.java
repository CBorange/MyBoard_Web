package com.ltj.myboard.service;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

public class BoardService{

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public Optional<Board> findBoardByID(int id) {
        return boardRepository.findBoardByID(id);
    }

    public List<Board> getAllBoards() {
        return boardRepository.getAllBoards();
    }

    public List<Board> getAllRootBoards() {
        return boardRepository.getAllRootBoards();
    }
}