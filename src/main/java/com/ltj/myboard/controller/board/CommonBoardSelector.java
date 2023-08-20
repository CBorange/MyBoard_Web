package com.ltj.myboard.controller.board;

import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RequiredArgsConstructor
public class CommonBoardSelector implements BoardSelector{
    private final PostService postService;

    @Override
    public List<FilteredPost> selectPostData(int boardId, int maxVisiblePostCntInPage, int pageNumber, String searchType, String searchValue,
                                             Ref<Integer> totalPageRef) {

        String[] searchConditions = BoardSelector.super.makeSearchConditions(searchType, searchValue);
        List<FilteredPost> postList = postService.findPost_UserParam(boardId,
                searchConditions[0], searchConditions[1], searchConditions[2],
                PageRequest.of(pageNumber - 1, maxVisiblePostCntInPage), totalPageRef);

        return postList;
    }
}
