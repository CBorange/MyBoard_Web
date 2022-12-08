package com.ltj.myboard.service;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<OrderedComment> findRootCommentInPost(int postID){
        List<OrderedComment> rets = commentRepository.findOrderedRootComment(postID);
        return rets;
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

    public Comment insertComment( int postID, Integer parentCommentID, String writerID, String content) throws SQLException {
        int generatedRowKey = commentRepository.insertComment(postID, parentCommentID, writerID, content);
        Optional<Comment> newComment = commentRepository.findCommentByID(generatedRowKey);
        return newComment.orElseThrow(() -> {
           return new SQLException("CommentService : insertComment Error, can't not found from generatedKey = " + generatedRowKey);
        });
    }
}
