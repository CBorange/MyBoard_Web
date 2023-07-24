package com.ltj.myboard.controller;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.dto.mypage.MyInfo;
import com.ltj.myboard.service.UserService;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController extends LayoutControllerBase{
    private final UserService userService;
    private final int MAX_VISIBLE_PAGE = 10;

    @GetMapping("/mypage/myinfo")
    public String MyPage_MyInfo(Model model){
        addLayoutModel_FragmentContent(model,"mypage/mypage_main.html","mypage_main");
        model.addAttribute("selectedTab", "myinfo");

        return LayoutViewPath;
    }

    @GetMapping("/mypage/notification")
    public String MyPage_Notification(Model model, @RequestParam(required = false, defaultValue = "1")int pageNumber){
        addLayoutModel_FragmentContent(model,"mypage/mypage_main.html","mypage_main");
        model.addAttribute("selectedTab", "notification");

        Ref<Integer> totalPageRef = new Ref<>();
        MyInfo userInfo = (MyInfo)model.getAttribute("userInfo");

        // 알림 데이터 획득
        List<UserNotification> notis = userService.getUserNotifications(userInfo.getUserId(),
                PageRequest.of(pageNumber - 1, MAX_VISIBLE_PAGE, Sort.by(Sort.Direction.DESC, "modifyDay")),
                totalPageRef);

        // 페이지 개수 구하기
        int pageCount = totalPageRef.getValue();
        if(pageCount == 0)
            pageCount = 1;
        if(pageNumber > pageCount)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, pageNumber + " page is out of bound");

        // 모델 추가
        model.addAttribute("notifications", notis);
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("pageCount", pageCount);

        return LayoutViewPath;
    }
}
