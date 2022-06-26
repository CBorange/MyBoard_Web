package com.ltj.myboard.controller;
import com.ltj.myboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController extends LayoutControllerBase {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping("/board/general")
    public String board_General(Model model) {
        addLayoutModel_FragmentContent(model,"board.html","board");

        return LayoutViewPath;
    }
}
