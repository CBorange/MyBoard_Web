package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.comment.SubmitCommentData;
import com.ltj.myboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/comment")
    public ResponseEntity submitComment(@RequestBody SubmitCommentData submitCommentData){
        try {
            Comment insertedComment = commentService.insertComment(submitCommentData.getPostID(),
                    submitCommentData.getParentCommentID(),
                    submitCommentData.getWriterID(),
                    submitCommentData.getContent());

            String redirectURI = String.format("/post/%d", submitCommentData.getPostID());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectURI);
            return new ResponseEntity<String>(headers, HttpStatus.SEE_OTHER);
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
