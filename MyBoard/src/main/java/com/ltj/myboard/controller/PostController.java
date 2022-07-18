package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.service.serviceinterface.BoardService;
import com.ltj.myboard.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class PostController extends LayoutControllerBase {
    private final BoardService boardService;
    private final PostService postService;

    @Autowired
    public PostController(BoardService boardService, PostService postService){
        this.boardService = boardService;
        this.postService = postService;
    }

    @GetMapping("/post")
    public String post_Request(Model model, @RequestParam int id){
        addLayoutModel_FragmentContent(model, "post.html", "post");

        // Post 정보 Model에 추가
        Optional<Post> foundPost = postService.findPostByID(id);
        foundPost.ifPresentOrElse((post) -> {
            model.addAttribute("postInfo", foundPost.get());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

        // Board 정보 Model에 추가
        Optional<Board> foundBoard = boardService.findBoardByID(foundPost.get().getBoardID());
        foundBoard.ifPresentOrElse((board) -> {
            model.addAttribute("boardInfo", foundBoard.get());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });
        return LayoutViewPath;
    }
}
