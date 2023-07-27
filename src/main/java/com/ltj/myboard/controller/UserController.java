package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.service.CommentService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/user/notification/{notificationId}/read")
    public ResponseEntity readNotification(@PathVariable int notificationId){
        UserNotification notification = null;
        notification = userService.getNotificationById(notificationId);
        userService.readNotification(notification);

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
}
