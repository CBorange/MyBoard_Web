package com.ltj.myboard.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyPageController extends LayoutControllerBase{

    @GetMapping("/mypage")
    public String MyPage_View(Model model){
        addLayoutModel_FragmentContent(model,"mypage/mypage_main.html","mypage_main");
        return LayoutViewPath;
    }
}
