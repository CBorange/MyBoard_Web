package com.ltj.myboard.controller;
import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class LayoutControllerBase {

    protected final String LayoutViewPath = "layout/default_layout.html";

    @Autowired
    private BoardService boardService;

    @ModelAttribute("rootBoards")
    public List<Board> rootBoards(){
        List<Board> rootBoards = boardService.getAllRootBoards();
        return rootBoards;
    }

    @ModelAttribute("userInfo")
    public User userInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if(principal == null) return null;
        if(!(principal instanceof UserDetailsImpl)) return null;

        UserDetailsImpl userDetails = (UserDetailsImpl)principal;
        User user = userDetails.getUser();
        return user;
    }

    /**
     * View 호출시에 Layout에 등록할 Page Fragment를 지정한다.
     * @param model MVC model 객체
     * @param pageFile 페이지 파일 경로
     * @param contentFragmentID fragment html파일에서 지정한 fragment 식별자
     */
    protected void addLayoutModel_FragmentContent(Model model, String pageFile, String contentFragmentID){
        model.addAttribute("layoutContent_Page", pageFile);
        model.addAttribute("layoutContent_Fragment", contentFragmentID);
    }
}
