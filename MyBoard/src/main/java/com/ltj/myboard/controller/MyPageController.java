package com.ltj.myboard.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MyPageController extends LayoutControllerBase{

    private void MakeModelFor_Notification(Model model){

    }

    @GetMapping("/mypage/{selectedTab}")
    public String MyPage_View(Model model, @PathVariable("selectedTab") String selectedTab){
        addLayoutModel_FragmentContent(model,"mypage/mypage_main.html","mypage_main");
        model.addAttribute("selectedTab", selectedTab);

        // 내정보(Do Nothing, LayoutControllerBase에서 유저정보 항상 반환함

        // 내 알림목록
        MakeModelFor_Notification(model);
        return LayoutViewPath;
    }
}
