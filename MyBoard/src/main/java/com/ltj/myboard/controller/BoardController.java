package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.dto.board.FilteredPost;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController extends LayoutControllerBase {
    private final BoardService boardService;
    private final PostService postService;
    private final int MAX_VISIBLE_PAGE_COUNT_INSESSION = 9;
    private final int MAX_VISIBLE_POST_COUNT_INPAGE = 25;

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

        // 검색 조건에 따라 게시글 리스트 Select
        List<FilteredPost> postList = postService.findPost_UserParam(id, searchMethod, searchCondition, sortOrderTarget, sortMethod);

        // 페이지 개수 구하기
        long postCount = postList.stream().count();
        int pageCount = Paginator.getPageCount(postCount, MAX_VISIBLE_POST_COUNT_INPAGE);

        // 현재 페이지의 세션 구하기
        int curSession = Paginator.getCurSessionByCurPage(pageNumber, MAX_VISIBLE_PAGE_COUNT_INSESSION);
        int endPageNoInCurSession = curSession * MAX_VISIBLE_PAGE_COUNT_INSESSION;
        int startPageNoInCurSession = endPageNoInCurSession - (MAX_VISIBLE_PAGE_COUNT_INSESSION - 1);
        if(pageCount < endPageNoInCurSession) endPageNoInCurSession = pageCount;


        // 검색된 게시글 리스트에서 현재 페이지에 해당하는 부분만 filtering
        List<FilteredPost> filteredPostList = postService.filterPostDataInCurPage(postList, pageNumber, MAX_VISIBLE_POST_COUNT_INPAGE);
        model.addAttribute("filteredPostList", filteredPostList);

        // 현재 페이지 정보 Model에 추가
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("endPageNoInCurSession", endPageNoInCurSession);
        model.addAttribute("startPageNoInCurSession", startPageNoInCurSession);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("searchMethod", searchMethod);
        model.addAttribute("searchCondition", searchCondition);
        model.addAttribute("sortOrderTarget", sortOrderTarget);
        model.addAttribute("sortMethod", sortMethod);
        return LayoutViewPath;
    }
}
