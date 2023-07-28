package com.ltj.myboard.service;

import com.ltj.myboard.domain.*;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.model.ActivityHistoryTypes;
import com.ltj.myboard.repository.CommentActivityHistoryRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentActivityHistoryRepository commentActivityHistoryRepository;
    private final UserService userService;

    public Comment findCommentById(int commentId){
        Optional<Comment> foundComment = commentRepository.findById(commentId);
        Comment found = foundComment.orElseThrow(() -> {
            throw new NoSuchElementException("cannot find " + commentId + "Comment");
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

            // 추천 기록 조회
            List<CommentActivityHistory> likesHistory = commentActivityHistoryRepository.findAllByTypeAndCommentId(ActivityHistoryTypes.Like.getValue(), rootComment.getId());
            newOrderedComment.setLikesCount(likesHistory.stream().count());

            // 비추천 기록 조회
            List<CommentActivityHistory> dislikesHistory = commentActivityHistoryRepository.findAllByTypeAndCommentId(ActivityHistoryTypes.Dislike.getValue(), rootComment.getId());
            newOrderedComment.setDislikesCount(dislikesHistory.stream().count());

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

    public List<OrderedComment> findCommentByWriterId(String writerId, PageRequest pageRequest, Ref<Integer> totalPageCntRet){
        Page<Comment> queryRet = commentRepository.findAllByWriterId(writerId, pageRequest);
        List<Comment> retList = queryRet.getContent();
        totalPageCntRet.setValue(queryRet.getTotalPages());

        List<OrderedComment> ret = new ArrayList<>();

        int idx =0;
        for (Comment comment : retList){
            OrderedComment newOrderedComment = new OrderedComment();
            newOrderedComment.setOrderedCommentNo(idx + 1);

            // 추천 기록 조회
            List<CommentActivityHistory> likesHistory = commentActivityHistoryRepository.findAllByTypeAndCommentId(ActivityHistoryTypes.Like.getValue(), comment.getId());
            newOrderedComment.setLikesCount(likesHistory.stream().count());

            // 비추천 기록 조회
            List<CommentActivityHistory> dislikesHistory = commentActivityHistoryRepository.findAllByTypeAndCommentId(ActivityHistoryTypes.Dislike.getValue(), comment.getId());
            newOrderedComment.setDislikesCount(dislikesHistory.stream().count());

            newOrderedComment.setCommentData(comment);

            ret.add(newOrderedComment);
            idx++;
        }

        return ret;
    }

    @Transactional
    public Comment insertComment( int postID, String postWriterId, Comment parentComment, String writerID, String writerNickname, String content) {
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
    }

    /**
     * 댓글 추천 적용*/
    public CommentActivityHistory applyLike(int commentId, String userId){
        long likeCountOnThis = commentActivityHistoryRepository.countByTypeAndCommentIdAndUserId(
                ActivityHistoryTypes.Like.getValue(), commentId, userId);
        if(likeCountOnThis > 0){
            throw new IllegalStateException("이미 추천하였습니다.");
        }

        long dislikeCountOnThis = commentActivityHistoryRepository.countByTypeAndCommentIdAndUserId(
                ActivityHistoryTypes.Dislike.getValue(), commentId, userId);
        if(dislikeCountOnThis > 0){
            throw new IllegalStateException("이미 비추천하였습니다. 추천 또는 비추천은 한번만 할 수 있습니다.");
        }

        CommentActivityHistory newHistory = new CommentActivityHistory();
        newHistory.setType(ActivityHistoryTypes.Like.getValue());
        newHistory.setCommentId(commentId);
        newHistory.setUserId(userId);
        newHistory.setCreatedDay(new Date());

        commentActivityHistoryRepository.save(newHistory);

        return newHistory;
    }

    /**
     * 추천 내역 삭제*/
    public void deleteLike(int commentId, String userId){
        CommentActivityHistory history = commentActivityHistoryRepository.findByTypeAndCommentIdAndUserId(
                ActivityHistoryTypes.Like.getValue(), commentId, userId)
                .orElseThrow(() -> new NoSuchElementException(commentId + " comment, " + userId + " user cannot found history"));

        commentActivityHistoryRepository.delete(history);
    }

    /**
     * 댓글 추천 적용*/
    public CommentActivityHistory applyDislike(int commentId, String userId){
        long dislikeCountOnThis = commentActivityHistoryRepository.countByTypeAndCommentIdAndUserId(
                ActivityHistoryTypes.Dislike.getValue(), commentId, userId);
        if(dislikeCountOnThis > 0){
            throw new IllegalStateException("이미 비추천하였습니다.");
        }

        long likeCountOnThis = commentActivityHistoryRepository.countByTypeAndCommentIdAndUserId(
                ActivityHistoryTypes.Like.getValue(), commentId, userId);
        if(likeCountOnThis > 0){
            throw new IllegalStateException("이미 추천하였습니다. 추천 또는 비추천은 한번만 할 수 있습니다.");
        }

        CommentActivityHistory newHistory = new CommentActivityHistory();
        newHistory.setType(ActivityHistoryTypes.Dislike.getValue());
        newHistory.setCommentId(commentId);
        newHistory.setUserId(userId);
        newHistory.setCreatedDay(new Date());

        commentActivityHistoryRepository.save(newHistory);

        return newHistory;
    }

    /**
     * 추천 내역 삭제*/
    public void deleteDislike(int commentId, String userId){
        CommentActivityHistory history = commentActivityHistoryRepository.findByTypeAndCommentIdAndUserId(
                        ActivityHistoryTypes.Dislike.getValue(), commentId, userId)
                .orElseThrow(() -> new NoSuchElementException(commentId + " comment, " + userId + " user cannot found history"));

        commentActivityHistoryRepository.delete(history);
    }
}
