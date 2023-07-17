package com.ltj.myboard.service;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService{

    private final BoardRepository boardRepository;

    public Optional<Board> findBoardByID(int id) {
        return boardRepository.findBoardById(id);
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public List<Board> getAllRootBoards(){
        return boardRepository.findAllByParentBoardIsNull();
    }

    public List<Board> getAllLeafBoards(){
        return boardRepository.findLeafBoards();
    }
}
