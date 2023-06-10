package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Paginator;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController extends LayoutControllerBase {
    private final BoardService boardService;
    private final PostService postService;
    private final int MAX_VISIBLE_PAGE_COUNT_INSESSION = 9;
    private final int MAX_VISIBLE_POST_COUNT_INPAGE = 25;

    @GetMapping("/board/{id}")
    public String board_Request(Model model, @PathVariable("id") int id,
                                             @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                             @RequestParam(required = false, defaultValue = "title") String searchType,
                                             @RequestParam(required = false, defaultValue = "") String searchValue){
        addLayoutModel_FragmentContent(model,"board.html","board");

        // log test
        log.info("board request");

        // Board 정보 Model에 추가
        Optional<Board> foundBoard = boardService.findBoardByID(id);
        foundBoard.ifPresent((board) -> {
            model.addAttribute("boardInfo", foundBoard.get());
        });

        // 검색조건 처리
        String searchConditions[] = {null, null, null}; // title, content, nickname 순
        if(searchType.equals("title")) searchConditions[0] = searchValue;
        if(searchType.equals("content")) searchConditions[1] = searchValue;
        if(searchType.equals("nickname")) searchConditions[2] = searchValue;

        // 검색 조건에 따라 게시글 리스트 Select
        Ref<Integer> totalPageRef = new Ref<>();
        List<FilteredPost> postList = postService.findPost_UserParam(id,
                searchConditions[0], searchConditions[1], searchConditions[2],
                PageRequest.of(pageNumber - 1, MAX_VISIBLE_POST_COUNT_INPAGE), totalPageRef);

        // 페이지 개수 구하기
        int pageCount = totalPageRef.getValue();
        if(pageCount == 0)
            pageCount = 1;
        if(pageNumber > pageCount)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, pageNumber + " page is out of bound");

        // 현재 페이지의 세션 구하기
        int curSession = Paginator.getCurSessionByCurPage(pageNumber, MAX_VISIBLE_PAGE_COUNT_INSESSION);
        int endPageNoInCurSession = curSession * MAX_VISIBLE_PAGE_COUNT_INSESSION;
        int startPageNoInCurSession = endPageNoInCurSession - (MAX_VISIBLE_PAGE_COUNT_INSESSION - 1);
        if(pageCount < endPageNoInCurSession) endPageNoInCurSession = pageCount;

        // 현재 페이지 정보 Model에 추가
        model.addAttribute("filteredPostList", postList);
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("endPageNoInCurSession", endPageNoInCurSession);
        model.addAttribute("startPageNoInCurSession", startPageNoInCurSession);
        model.addAttribute("pageCount", pageCount);

        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        return LayoutViewPath;
    }
}
