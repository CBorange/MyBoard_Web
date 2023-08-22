package com.ltj.myboard.controller;
import com.ltj.myboard.controller.board.BoardSelector;
import com.ltj.myboard.controller.board.BoardSelectorFactory;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.BoardTypeDefiner;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.service.TestService;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController extends LayoutControllerBase {

    private final TestService testService;
    private final BoardService boardService;
    private final PostService postService;

    private final int MAX_VISIBLE_POST = 10;

    @GetMapping("/version")
    public String versionInfoPage(Model model){
        addLayoutModel_FragmentContent(model, "versioninfo.html", "versioninfo");
        return LayoutViewPath;
    }

    @GetMapping("/")
    public String home(Model model){
        addLayoutModel_FragmentContent(model,"home.html", "home");

        // Leaf 게시판(자식이 없는 말단 게시판) 목록 모델에 추가
        List<Board> leafBoards = boardService.getAllLeafBoards();
        List<Board> singleColumnBoards = new ArrayList<>();
        List<List<Board>> doubleColumnBoards = new ArrayList<>();

        List<Board> innerDoubleColumnBoards = new ArrayList<>(2);
        Iterator<Board> boardIterator = leafBoards.iterator();
        while(boardIterator.hasNext()){
            Board currentBoard = boardIterator.next();
            // 일반 타입 게시판의 경우 한 줄에 2개씩 표출되어야 함
            if(currentBoard.getBoardType().getType() == BoardTypeDefiner.Common){
                if(innerDoubleColumnBoards.stream().count() < 2){
                    innerDoubleColumnBoards.add(currentBoard);
                    if(innerDoubleColumnBoards.stream().count() == 2 ||
                        !boardIterator.hasNext()){
                        doubleColumnBoards.add(innerDoubleColumnBoards);
                        innerDoubleColumnBoards = new ArrayList<>(2);
                    }
                }
            }
            // 그 외 타입 게시판(베스트, 공지사항)의 경우 한 줄에 1개씩 표출되어야 함
            else{
                singleColumnBoards.add(currentBoard);
            }
        }

        // 만들어진 Single, Double 컬럼 게시판 리스트 model에 추가
        model.addAttribute("singleColumnBoards", singleColumnBoards);
        model.addAttribute("doubleColumnBoards", doubleColumnBoards);

        // Leaf 게시판 별로 Post 데이터 조회하여 Map(boardID is key)으로 반환
        Map<Integer, List<FilteredPost>> postMap = new HashMap<Integer, List<FilteredPost>>();
        for(Board leaf : leafBoards){
            // 게시판의 타입에 따른 Selector가 생성되어 조회 로직이 실행된다
            BoardSelectorFactory selectorFactory = new BoardSelectorFactory();
            BoardSelector selector = selectorFactory.createInstance(postService, leaf.getBoardType().getType());

            Ref<Integer> totalPageRef = new Ref<>();
            List<FilteredPost> result = selector.selectPostData(
                    leaf.getId(), MAX_VISIBLE_POST, 1, "title", "", totalPageRef);

            postMap.put(leaf.getId(), result);
        }
        model.addAttribute("postMap", postMap);
        return LayoutViewPath;
    }

/*    @GetMapping("/test")
    public ResponseEntity test(){
        testService.DoTest();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test")
    public ResponseEntity test2(){
        return ResponseEntity.badRequest().body("Test2 Error");
        //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test2 Error");
    }*/

    @GetMapping("/invalid")
    public String invalideSessionPage(Model model) {
        return "InvalidSession";
    }

    @GetMapping("/expired")
    public String expiredSessionPage(Model model) {
        return "ExpiredSession";
    }
}
