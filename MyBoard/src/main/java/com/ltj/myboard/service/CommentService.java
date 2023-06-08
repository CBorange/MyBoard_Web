package com.ltj.myboard.service;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<OrderedComment> findRootCommentInPost(int postID){
        List<Comment> rootComments = commentRepository.findAllByPostIdAndParentCommentIsNull(
                postID,
                Sort.by(Sort.Direction.ASC,
                "createdDay"));

        List<OrderedComment> ret = new ArrayList<>();

        int idx = 0;
        for(Comment rootComment : rootComments){
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

    public List<OrderedComment> filterCommentDataInCurPage(List<OrderedComment> sourceList, int curPage,
                                                           int maxVisibleCommentCountInPage){
        // 현재 페이지의 첫번째, 마지막 게시글 범위 지정
        int pageStartRowNo = (curPage - 1) * maxVisibleCommentCountInPage;
        int pageEndRowNo = curPage * maxVisibleCommentCountInPage;

        // 현재 페이지의 범위에 해당하는 댓글을 Stream으로 filtering 한다
        List<OrderedComment> filteredCommentList = sourceList.stream().filter((source) -> {
            if(source.getOrderedCommentNo() >= pageStartRowNo &&
                    source.getOrderedCommentNo() <= pageEndRowNo)
                return true;
            return false;
        }).collect(Collectors.toList());

        return filteredCommentList;
    }

    public Comment insertComment( int postID, Comment parentComment, String writerID, String content) {
        try {
            Comment newComment = new Comment();
            newComment.setPostId(postID);
            newComment.setParentComment(parentComment);
            newComment.setWriterId(writerID);
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
