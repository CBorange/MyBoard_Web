package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.dto.auth.ChangeUserInfoRequest;
import com.ltj.myboard.dto.auth.ChangeUserPasswordRequest;
import com.ltj.myboard.dto.auth.RegistUserRequest;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final AuthService authService;
    private final UserNotiService userNotiService;
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/user/notification/{notificationId}/read")
    public ResponseEntity readNotification(@PathVariable int notificationId){
        UserNotification notification = null;
        notification = userNotiService.getNotificationById(notificationId);
        userNotiService.readNotification(notification);

        switch (notification.getContentType()){
            // 게시글 추천/비추천
            case "post":{
                // Post 존재하는 지 조회, 없으면 예외 throw, 프론트에서 처리
                Post foundPost = postService.findPostByID(notification.getContentId());
                String redirectURL = String.format("/post/%d", foundPost.getId());

                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", redirectURL);
                return new ResponseEntity(headers, HttpStatus.SEE_OTHER);
            }
            // 댓글 알림
            case "comment":{
                // Comment 존재하는 지 조회, 없으면 예외 throw, 프론트에서 처리
                Comment foundComment = commentService.findCommentById(notification.getContentId());
                String redirectURL = String.format("/post/%d?focusCommentId=%d", foundComment.getPostId(), foundComment.getId());

                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", redirectURL);
                return new ResponseEntity(headers, HttpStatus.SEE_OTHER);
            }
            default:
                throw new NoSuchElementException(String.format("a notification[id : %d] doesn't have content type", notificationId));
        }
    }

    // 회원가입 기능 실행
    @PostMapping("/user")
    @ResponseBody
    public ResponseEntity register(@Valid @RequestBody RegistUserRequest request){
        User newUser = authService.registerUser(request);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

    // 유저정보 수정 기능 실행
    @PatchMapping("/user")
    public ResponseEntity changeUserInfo(HttpServletRequest request, @Valid @RequestBody ChangeUserInfoRequest requestDTO){
        User changedUser = authService.changeUserInfo(requestDTO);

        logoutManually(request);
        return new ResponseEntity<User>(changedUser, HttpStatus.OK);
    }

    // 유저 비밀번호 변경 기능 실행
    @PutMapping("/user/password")
    public ResponseEntity changePassword(HttpServletRequest request, @Valid @RequestBody ChangeUserPasswordRequest requestDTO){
        String changedPassword = authService.changeUserPassword(requestDTO);

        logoutManually(request);
        return new ResponseEntity<String>(changedPassword, HttpStatus.OK);
    }

    // 수동으로 Logout 처리 실행
    private void logoutManually(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        if(session != null) {
            session.invalidate();
        }

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies) {
                cookie.setMaxAge(0);
            }
        }
    }

}
