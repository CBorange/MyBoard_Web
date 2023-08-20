package com.ltj.myboard.controller.board;

import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RequiredArgsConstructor
public class BestBoardSelector implements BoardSelector{
    private final PostService postService;

    @Override
    public List<FilteredPost> selectPostData(int boardId, int maxVisiblePostCntInPage, int pageNumber, String searchType, String searchValue,
                                             Ref<Integer> totalPageRef) {
        // 베스트 게시판은 해당 게시판에 게시글이 등록되는 형식이 아니기 때문에 boardId가 의미가 없다. 사용안함
        String[] searchConditions = BoardSelector.super.makeSearchConditions(searchType, searchValue);
        List<FilteredPost> postList = postService.findPost_Best(
                searchConditions[0], searchConditions[1], searchConditions[2],
                PageRequest.of(pageNumber - 1, maxVisiblePostCntInPage), totalPageRef);

        return postList;
    }
}