package com.ltj.myboard;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.service.BoardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void 루트_게시판_읽어오기(){
        List<Board> boards = boardService.getAllBoards();
        for (Board item : boards){
            System.out.println(item.toString());
        }
    }

    @Test
    public void 전체_게시판_읽어오기(){

    }

    @Test
    public void ID로_게시판찾기(){

    }
}
