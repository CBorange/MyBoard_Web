package com.ltj.myboard.service.serviceinterface;

import com.ltj.myboard.domain.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    List<Comment> findAllCommentInPost(int postID);
    
}
