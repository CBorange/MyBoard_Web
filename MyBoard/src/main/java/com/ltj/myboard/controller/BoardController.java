package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.service.serviceinterface.BoardService;
import com.ltj.myboard.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BoardController extends LayoutControllerBase {
    private final BoardService boardService;
    private final PostService postService;
    private final int MAX_VISIBLE_POST_COUNT = 20;

    @Autowired
    public BoardController(BoardService boardService, PostService postService){
        this.boardService = boardService;
        this.postService = postService;
    }

    @GetMapping("/board")
    public String board_Request(Model model, @RequestParam() int id,
                                             @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                             @RequestParam(required = false, defaultValue = "Title,") String[] searchMethodConditionPair,
                                             @RequestParam(required = false, defaultValue = "ModifyDay") String sortOrderTarget) {
        addLayoutModel_FragmentContent(model,"board.html","board");

        // Board 정보 Model에 추가
        Optional<Board> foundBoard = boardService.findBoardByID(id);
        foundBoard.ifPresent((board) -> {
            model.addAttribute("boardInfo", foundBoard.get());
        });

        // 게시글 리스트 Select 하여 Model에 추가
/*        List<Post> postList = postService.findPost_UserParam(id, pageNumber);
        model.addAttribute("postList", postList);*/
        return LayoutViewPath;
    }
}
