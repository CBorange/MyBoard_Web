package com.ltj.myboard.service;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.repository.CommentRepository;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment findCommentById(int commentId){
        Optional<Comment> foundComment = commentRepository.findById(commentId);
        Comment found = foundComment.orElseThrow(() -> {
            throw new IllegalStateException("cannot find " + commentId + "Comment");
        });
        return found;
    }

    public List<OrderedComment> findRootCommentInPost(int postID, PageRequest pageRequest, Ref<Integer> totalPageCntRet){
        Page<Comment> rootComments = commentRepository.findAllByPostIdAndParentCommentIsNull(
                postID,
                pageRequest);
        List<Comment> retList = rootComments.getContent();
        totalPageCntRet.setValue(rootComments.getTotalPages());

        List<OrderedComment> ret = new ArrayList<>();

        int idx = 0;
        for(Comment rootComment : retList){
            OrderedComment newOrderedComment = new OrderedComment();
            newOrderedComment.setOrderedCommentNo(idx + 1);
            newOrderedComment.setCommentData(rootComment);

            ret.add(newOrderedComment);
            idx++;
        }

        return ret;
    }

    public long getCommentCountByPost(int postId){
        return commentRepository.countByPostId(postId);
    }

    public Comment insertComment( int postID, Comment parentComment, String writerID, String writerNickname, String content) {
        try {
            Comment newComment = new Comment();
            newComment.setPostId(postID);
            newComment.setParentComment(parentComment);
            newComment.setWriterId(writerID);
            newComment.setWriterNickname(writerNickname);
            newComment.setContent(content);
            newComment.setCreatedDay(new Date());
            newComment.setModifyDay(new Date());

            commentRepository.save(newComment);
            return newComment;
        } catch (Exception e){
            String msg = "CommentService : insertComment Error " + e.getMessage();
            log.error(msg);
            throw new IllegalStateException(msg);
        }
    }
}
