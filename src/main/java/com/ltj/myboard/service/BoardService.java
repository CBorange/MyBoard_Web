package com.ltj.myboard.service;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardService{

    private final BoardRepository boardRepository;

    public Board findBoardByID(int id) {
        return boardRepository.findBoardById(id).orElseThrow(
            () -> {
                String msg = String.format("Board Id [%d]에 해당하는 Board 데이터를 찾을 수 없습니다.");
                log.info(msg);
                throw new NoSuchElementException(msg);
            }
        );
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
