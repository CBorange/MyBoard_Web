package com.ltj.myboard.controller.board;
import com.ltj.myboard.controller.ControllerPaginationUtil;
import com.ltj.myboard.controller.LayoutControllerBase;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Paginator;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Filter;

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

        // Board 정보 Model에 추가
        Board foundBoard = boardService.findBoardByID(id);
        model.addAttribute("boardInfo", foundBoard);

        // 검색조건 Model 추가
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);

        // 검색 조건에 따라 게시글 리스트 Select
        // 게시판 타입에 따라 조회 다르게 처리한다.
        // 검색조건 처리
        Ref<Integer> totalPageRef = new Ref<>();
        BoardSelectorFactory selectorFactory = new BoardSelectorFactory();
        BoardSelector selector = selectorFactory.createInstance(postService, foundBoard.getBoardType().getType());
        List<FilteredPost> postList = selector.selectPostData(id, MAX_VISIBLE_POST_COUNT_INPAGE, pageNumber, searchType, searchValue, totalPageRef);

        // 페이지 처리
        ControllerPaginationUtil paginationUtil = new ControllerPaginationUtil();
        paginationUtil.makePageModelWithSession(model, postList, totalPageRef.getValue(),
                pageNumber, MAX_VISIBLE_PAGE_COUNT_INSESSION);

        return LayoutViewPath;
    }
}
