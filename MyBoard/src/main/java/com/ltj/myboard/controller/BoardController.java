package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.dto.board.FilteredPost;
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
                                             @RequestParam(required = false, defaultValue = "Title") String searchMethod,
                                             @RequestParam(required = false, defaultValue = "") String searchCondition,
                                             @RequestParam(required = false, defaultValue = "ModifyDay") String sortOrderTarget,
                                             @RequestParam(required = false, defaultValue = "ASC") String sortMethod){
        addLayoutModel_FragmentContent(model,"board.html","board");

        // Board 정보 Model에 추가
        Optional<Board> foundBoard = boardService.findBoardByID(id);
        foundBoard.ifPresent((board) -> {
            model.addAttribute("boardInfo", foundBoard.get());
        });

        // 게시글 리스트 Select 하여 Model에 추가
        // 페이지 개수 구하기
        List<FilteredPost> postList = postService.findPost_UserParam(id, searchMethod, searchCondition, sortOrderTarget, sortMethod);
        int pageCount = postService.getPageCountOnPostList(postList, MAX_VISIBLE_POST_COUNT);
        if(pageCount < 1) pageCount = 1;

        // Filtering 된 게시글 리스트에서 현재 페이지에 해당하는 부분만 선택
        List<FilteredPost> filteredPostList = postService.filterPostDataOnCurPage(postList, pageCount, pageNumber, MAX_VISIBLE_POST_COUNT);
        model.addAttribute("filteredPostList", filteredPostList);

        // 현재 페이지 정보 Model에 추가
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("searchMethod", searchMethod);
        model.addAttribute("searchCondition", searchCondition);
        model.addAttribute("sortOrderTarget", sortOrderTarget);
        model.addAttribute("sortMethod", sortMethod);
        return LayoutViewPath;
    }
}
