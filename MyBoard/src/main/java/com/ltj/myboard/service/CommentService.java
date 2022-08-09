package com.ltj.myboard.service;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.board.FilteredPost;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CommentService {
    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

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
}
