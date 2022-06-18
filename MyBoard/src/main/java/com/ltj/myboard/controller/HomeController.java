package com.ltj.myboard.controller;
import com.ltj.myboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final BoardService boardService;

    @Autowired
    public HomeController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String home(){
        return "layout/default_layout.html";
    }
}
