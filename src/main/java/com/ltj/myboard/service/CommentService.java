package com.ltj.myboard.service;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.repository.CommentRepository;
import com.ltj.myboard.repository.UserNotificationRepository;
import com.ltj.myboard.util.Ref;
import com.ltj.myboard.util.UserNotiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserService userService;

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

    public List<Comment> getAllRootCommentsInPost(int postId){
        List<Comment> rootComments = commentRepository.findAllByPostIdAndParentCommentIsNull(postId);
        return rootComments;
    }

    public long getCommentCountByPost(int postId){
        return commentRepository.countByPostId(postId);
    }

    @Transactional
    public Comment insertComment( int postID, String postWriterId, Comment parentComment, String writerID, String writerNickname, String content) {
        try {
            // 댓글 작성
            Comment newComment = new Comment();
            newComment.setPostId(postID);
            newComment.setParentComment(parentComment);
            newComment.setWriterId(writerID);
            newComment.setWriterNickname(writerNickname);
            newComment.setContent(content);
            newComment.setCreatedDay(new Date());
            newComment.setModifyDay(new Date());

            commentRepository.save(newComment);

            // 알림 보내기
            if(parentComment == null){  // 신규 댓글 -> 게시글 작성자 한테 알림 보내기
                userService.makeNotificationForComment(writerID, writerNickname, postWriterId, content, newComment.getId());
            } else{ // 대댓글 -> 원댓글 작성자 한테 알림 보내기
                userService.makeNotificationForSubComment(writerID, writerNickname, parentComment.getWriterId(), content, newComment.getId());
            }

            return newComment;
        } catch (Exception e){
            String msg = "CommentService : insertComment Error " + e.getMessage();
            log.error(msg);
            throw new IllegalStateException(msg);
        }
    }
}
