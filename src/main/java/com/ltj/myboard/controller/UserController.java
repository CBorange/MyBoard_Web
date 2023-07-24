package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.service.CommentService;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping("/user/notification/{notificationId}/read")
    public ResponseEntity readNotification(@PathVariable int notificationId){
        UserNotification notification = null;
        try{
            notification = userService.getNotificationById(notificationId);
            userService.readNotification(notification);

            switch (notification.getContentType()){
                // 게시글 추천/비추천
                case "post":{
                    String redirectURL = String.format("/post/%d", notification.getContentId());

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", redirectURL);
                    return new ResponseEntity(headers, HttpStatus.SEE_OTHER);
                }
                // 댓글 알림
                case "comment":{
                    Comment foundComment = commentService.findCommentById(notification.getContentId());
                    String redirectURL = String.format("/post/%d?focusCommentId=%d", foundComment.getPostId(), foundComment.getId());

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Location", redirectURL);
                    return new ResponseEntity(headers, HttpStatus.SEE_OTHER);
                }
                default:
                    return new ResponseEntity(String.format("a notification[id : %d] doesn't have content type", notificationId), HttpStatus.NOT_FOUND);
            }
        }
        catch (IllegalStateException e){
            int contentId = notification != null ? notification.getContentId() : -1;
            return new ResponseEntity(String.format("can not found notification or content data by id [notification id : %d] [content id : %d]",
                    notificationId, contentId) , HttpStatus.BAD_REQUEST);
        }
    }
}
