package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.comment.SubmitCommentData;
import com.ltj.myboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity submitComment(@RequestBody SubmitCommentData submitCommentData){
        try {
            Comment insertedComment = commentService.insertComment(submitCommentData.getPostId(),
                    submitCommentData.getParentCommentId(),
                    submitCommentData.getWriterId(),
                    submitCommentData.getContent());

            String redirectURI = String.format("/post/%d", submitCommentData.getPostId());

            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
