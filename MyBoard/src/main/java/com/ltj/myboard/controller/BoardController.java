package com.ltj.myboard.controller;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController extends LayoutControllerBase {
    private final BoardService boardService;
    private final PostService postService;

    @Autowired
    public BoardController(BoardService boardService, PostService postService){
        this.boardService = boardService;
        this.postService = postService;
    }

    @GetMapping("/board")
    public String board_Request(Model model, @RequestParam int id) {
        addLayoutModel_FragmentContent(model,"board.html","board");

        //


        return LayoutViewPath;
    }
}
