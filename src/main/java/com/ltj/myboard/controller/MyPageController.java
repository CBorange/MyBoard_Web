package com.ltj.myboard.controller;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.dto.mypage.MyInfo;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.service.CommentService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.service.UserService;
import com.ltj.myboard.util.Paginator;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MyPageController extends LayoutControllerBase{
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    // 게시글
    private final int MAX_VISIBLE_POST_COUNT_INPAGE = 15;
    private final int MAX_VISIBLE_POST_PAGE_COUNT_INSESSION = 9;

    // 댓글
    private final int MAX_VISIBLE_COMMENT_COUNT_INPAGE = 15;
    private final int MAX_VISIBLE_COMMENT_PAGE_COUNT_INSESSION = 9;

    // 알림
    private final int NOTIFICATION_MAX_VISIBLE_IN_PAGE = 15;

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
                PageRequest.of(pageNumber - 1, NOTIFICATION_MAX_VISIBLE_IN_PAGE, Sort.by(Sort.Direction.DESC, "modifyDay")),
                totalPageRef);

        // 페이지 개수 구하기
        int pageCount = totalPageRef.getValue();
        if(pageCount == 0)
            pageCount = 1;
        if(pageNumber > pageCount)
            throw new IllegalArgumentException(pageNumber + " page is out of bound");

        // 모델 추가
        model.addAttribute("notifications", notis);
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("pageCount", pageCount);

        return LayoutViewPath;
    }

    @GetMapping("/mypage/post")
    public String MyPage_Post(Model model, @RequestParam(required = false, defaultValue = "1")int pageNumber){
        addLayoutModel_FragmentContent(model,"mypage/mypage_main.html","mypage_main");

        model.addAttribute("selectedTab", "post");

        Ref<Integer> totalPageRef = new Ref<>();
        MyInfo userInfo = (MyInfo)model.getAttribute("userInfo");

        // 작성 게시글 리스트 Select
        List<FilteredPost> postList = postService.findPostByWriterId(userInfo.getUserId(),
                PageRequest.of(pageNumber - 1, MAX_VISIBLE_POST_COUNT_INPAGE), totalPageRef);

        // 페이지 개수 구하기
        int pageCount = totalPageRef.getValue();
        if(pageCount == 0)
            pageCount = 1;
        if(pageNumber > pageCount)
            throw new IllegalArgumentException(pageNumber + " page is out of bound");

        // 현재 페이지의 세션 구하기
        int curSession = Paginator.getCurSessionByCurPage(pageNumber, MAX_VISIBLE_POST_PAGE_COUNT_INSESSION);
        int endPageNoInCurSession = curSession * MAX_VISIBLE_POST_PAGE_COUNT_INSESSION;
        int startPageNoInCurSession = endPageNoInCurSession - (MAX_VISIBLE_POST_PAGE_COUNT_INSESSION - 1);
        if(pageCount < endPageNoInCurSession) endPageNoInCurSession = pageCount;

        // 모델 추가
        model.addAttribute("filteredPostList", postList);
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("endPageNoInCurSession", endPageNoInCurSession);
        model.addAttribute("startPageNoInCurSession", startPageNoInCurSession);
        model.addAttribute("pageCount", pageCount);

        return LayoutViewPath;
    }

    @GetMapping("/mypage/comment")
    public String MyPage_Comment(Model model, @RequestParam(required = false, defaultValue = "1")int pageNumber){
        addLayoutModel_FragmentContent(model,"mypage/mypage_main.html","mypage_main");

        model.addAttribute("selectedTab", "comment");

        Ref<Integer> totalPageRef = new Ref<>();
        MyInfo userInfo = (MyInfo)model.getAttribute("userInfo");

        // 작성 게시글 리스트 Select
        List<OrderedComment> commentList = commentService.findCommentByWriterId(userInfo.getUserId(),
                PageRequest.of(pageNumber - 1, MAX_VISIBLE_COMMENT_COUNT_INPAGE), totalPageRef);

        // 페이지 개수 구하기
        int pageCount = totalPageRef.getValue();
        if(pageCount == 0)
            pageCount = 1;
        if(pageNumber > pageCount)
            throw new IllegalArgumentException(pageNumber + " page is out of bound");

        // 현재 페이지의 세션 구하기
        int curSession = Paginator.getCurSessionByCurPage(pageNumber, MAX_VISIBLE_COMMENT_PAGE_COUNT_INSESSION);
        int endPageNoInCurSession = curSession * MAX_VISIBLE_COMMENT_PAGE_COUNT_INSESSION;
        int startPageNoInCurSession = endPageNoInCurSession - (MAX_VISIBLE_COMMENT_PAGE_COUNT_INSESSION - 1);
        if(pageCount < endPageNoInCurSession) endPageNoInCurSession = pageCount;

        // 모델 추가
        model.addAttribute("orderedCommentList", commentList);
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("endPageNoInCurSession", endPageNoInCurSession);
        model.addAttribute("startPageNoInCurSession", startPageNoInCurSession);
        model.addAttribute("pageCount", pageCount);

        return LayoutViewPath;
    }
}
