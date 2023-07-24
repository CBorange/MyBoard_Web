package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController extends LayoutControllerBase {

    private final BoardService boardService;
    private final PostService postService;

    @GetMapping("/version")
    public String versionInfoPage(Model model){
        addLayoutModel_FragmentContent(model, "versioninfo.html", "versioninfo");
        return LayoutViewPath;
    }

    @GetMapping("/")
    public String home(Model model){
        addLayoutModel_FragmentContent(model,"home.html", "home");

        // Leaf 게시판(자식이 없는 말단 게시판) 목록 모델에 추가
        // 게시판 2개씩 묶어서 전달(View 에서 출력할 때 Row 1개당 Column 2개씩 해서 출력함)
        List<Board> leafBoards = boardService.getAllLeafBoards();

        Queue<Board> leafBoardQueue = new LinkedList<Board>(leafBoards);
        List<List<Board>> leafBoardBundle = new ArrayList<List<Board>>();

        int maxCntInEachBundle = 2;     // 묶음에 들어가는 게시판 개수
        int addCntInCurBundle = 0;      // 현재 묶음에 들어간 개시판 개수
        List<Board> curBundle = null; // 현재 묶음
        while(!leafBoardQueue.isEmpty()) {
            Board leafBoard = leafBoardQueue.poll();
            if(leafBoard == null) break;

            if(addCntInCurBundle == maxCntInEachBundle)
                addCntInCurBundle = 0;

            if(addCntInCurBundle == 0){
                curBundle = new ArrayList<Board>();
                leafBoardBundle.add(curBundle);
            }

            curBundle.add(leafBoard);
            addCntInCurBundle++;
        }
        model.addAttribute("leafBoardBundle", leafBoardBundle);

        // Leaf 게시판 별로 Post 데이터 조회하여 Map(boardID is key)으로 반환
        Map<Integer, List<FilteredPost>> postMap = new HashMap<Integer, List<FilteredPost>>();
        for(List<Board> leafPair : leafBoardBundle){
            for(Board leaf : leafPair){
                List<FilteredPost> result = postService.getLastestPost(leaf.getId(), 10);
                postMap.put(leaf.getId(), result);
            }
        }
        model.addAttribute("postMap", postMap);

        // 베스트 게시판 게시글 데이터
        Ref<Integer> totalPageRef = new Ref<>();
        List<FilteredPost> bestPosts = postService.findPost_Best("", null, null,
                PageRequest.of(0, 10), totalPageRef);
        model.addAttribute("bestPosts", bestPosts);

        return LayoutViewPath;
    }

    @GetMapping("/invalid")
    public String invalideSessionPage(Model model) {
        return "InvalidSession";
    }

    @GetMapping("/expired")
    public String expiredSessionPage(Model model) {
        return "ExpiredSession";
    }
}
