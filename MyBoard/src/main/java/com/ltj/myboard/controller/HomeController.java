package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    private final BoardService boardService;

    @Autowired
    public HomeController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String home(Model model){
        List<Board> rootBoards = boardService.getAllRootBoards();
        model.addAttribute("rootBoards", rootBoards);

        return "layout/default_layout.html";
    }
}
