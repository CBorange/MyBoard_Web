package com.ltj.myboard.service;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Override
    public Optional<Board> findBoardByID(int id) {
        return null;
    }

    @Override
    public List<Board> getAllBoards() {
        return boardRepository.getAllBoards();
    }

    @Override
    public List<Board> getAllRootBoards() {
        return boardRepository.getAllRootBoards();
    }
}
