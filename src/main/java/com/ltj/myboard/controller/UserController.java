package com.ltj.myboard.controller;

import com.ltj.myboard.domain.*;
import com.ltj.myboard.dto.auth.ChangeUserInfoRequest;
import com.ltj.myboard.dto.auth.ChangeUserPasswordRequest;
import com.ltj.myboard.dto.auth.FindUserResult;
import com.ltj.myboard.dto.auth.RegistUserRequest;
import com.ltj.myboard.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final EmailService emailService;
    private final UserNotiService userNotiService;
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @PatchMapping("/user/notification/{notificationId}/read")
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

    @PutMapping("/user/notification/read")
    public ResponseEntity readAllNotification(@RequestParam String userId){
        userNotiService.readAllNotification(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원가입 기능 실행
    @PostMapping("/user")
    @ResponseBody
    public ResponseEntity register(@Valid @RequestBody RegistUserRequest request){
        User newUser = userService.registerUser(request);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

    // 유저정보 수정 기능 실행
    @PatchMapping("/user")
    public ResponseEntity changeUserInfo(HttpServletRequest request, @Valid @RequestBody ChangeUserInfoRequest requestDTO){
        User changedUser = userService.changeUserInfo(requestDTO);

        logoutManually(request);
        return new ResponseEntity<User>(changedUser, HttpStatus.OK);
    }

    // 유저 비밀번호 변경 기능 실행
    @PutMapping("/user/password")
    public ResponseEntity changePassword(HttpServletRequest request, @Valid @RequestBody ChangeUserPasswordRequest requestDTO){
        String changedPassword = userService.changeUserPassword(requestDTO);

        logoutManually(request);
        return new ResponseEntity<String>(changedPassword, HttpStatus.OK);
    }

    @PostMapping("/user/find")
    public ResponseEntity findUserInfo(@RequestParam("userEmail") String userEmail){
        // Redis에 유저정보 탐색 대기 저장
        String uniqueLink = userService.makeFindUserRequestAndSetToPending(userEmail);

        // 메일로 비밀번호 초기화 링크 전송
        try{
            emailService.sendFindUserConfirmMail(userEmail, uniqueLink);
        } catch (MessagingException e){
            // Global Exception Handler가 처리할 수 있도록 IllegalStateException으로 변환
            throw new IllegalStateException("메일 전송 실패 : " + e.getMessage());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/user/password/change-temporary")
    public ResponseEntity changePasswordByFindUserRequest(@RequestParam("linkParam") String linkParam){
        // 유저의 비밀번호를 임시 비밀번호로 변경한다.
        // 유저가 계정찾기를 시도했을 때 생성하여 Redis DB에 저장된 랜덤 유니크값 으로 다시 유저를 조회해서
        // 해당 유저의 비밀번호값을 랜덤 문자열로 변경시킨다.
        FindUserResult ret = userService.changeUserPasswordTemporallyByFindUserRequest(linkParam);

        // 변경된 계정 반환
        return new ResponseEntity(ret, HttpStatus.OK);
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
