package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController extends LayoutControllerBase {

    private final BoardService boardService;

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

        return LayoutViewPath;
    }
}
