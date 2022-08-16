package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/submitcomment")
    public String submitComment(@RequestParam() int postID,
                                @RequestParam(required = false) Integer parentCommentID,
                                @RequestParam() String writerID,
                                @RequestParam() String content){
        try {
            Comment insertedComment = commentService.insertComment(postID, parentCommentID, writerID, content);
            String redirectURI = String.format("redirect:/post?id=%d", postID);
            return redirectURI;
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
