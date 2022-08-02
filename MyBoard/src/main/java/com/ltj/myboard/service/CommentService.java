package com.ltj.myboard.service;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public class CommentService {
    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public List<Comment> findCommentInPost(int postID){
        List<Comment> rets = commentRepository.findAllRootComment(postID);
        return rets;
    }
}
