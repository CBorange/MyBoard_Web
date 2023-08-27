package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.domain.CommentActivityHistory;
import com.ltj.myboard.dto.comment.SubmitCommentData;
import com.ltj.myboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity submitComment(@RequestBody SubmitCommentData submitCommentData){
        Comment foundParentComment;
        try{
            foundParentComment = commentService.findCommentById(submitCommentData.getParentCommentId());
        } catch (NoSuchElementException e){
            if(submitCommentData.isSubComment()){
                throw new NoSuchElementException(e.getMessage());
            } else{
                foundParentComment = null;
            }
        }

        Comment insertedComment = commentService.insertComment(
                submitCommentData.getPostId(),
                submitCommentData.getPostWriterId(),
                foundParentComment,
                submitCommentData.getWriterId(),
                submitCommentData.getContent());

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity deleteComment(@PathVariable("id") int commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/comment/{id}/content")
    public ResponseEntity modifyComment(@PathVariable("id") int commentId, @RequestParam("newContent") String newContent){
        commentService.modifyCommentContent(commentId, newContent);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/comment/{id}/like")
    public ResponseEntity applyLikeComment(@PathVariable("id") int id, @RequestParam("userId") String userId){
        CommentActivityHistory history = commentService.applyLike(id, userId);
        return new ResponseEntity(history, HttpStatus.CREATED);
    }

    @DeleteMapping("/comment/{id}/like")
    public ResponseEntity deleteLikeComment(@PathVariable("id") int id, @RequestParam("userId") String userId){
        commentService.deleteLike(id, userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/comment/{id}/dislike")
    public ResponseEntity applyDislikeComment(@PathVariable("id") int id, @RequestParam("userId") String userId){
        CommentActivityHistory history = commentService.applyDislike(id, userId);
        return new ResponseEntity(history, HttpStatus.CREATED);
    }

    @DeleteMapping("/comment/{id}/dislike")
    public ResponseEntity deleteDislikeComment(@PathVariable("id") int id, @RequestParam("userId") String userId){
        commentService.deleteDislike(id, userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
