package com.ltj.myboard.controller.board;

import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.util.Ref;

import java.util.List;

public interface BoardSelector {
    public default String[] makeSearchConditions(String searchType, String searchValue){
        String searchConditions[] = {null, null, null}; // title, content, nickname 순
        if(searchType.equals("title")) searchConditions[0] = searchValue;
        if(searchType.equals("content")) searchConditions[1] = searchValue;
        if(searchType.equals("nickname")) searchConditions[2] = searchValue;

        return searchConditions;
    }

    public List<FilteredPost> selectPostData(int boardId, int maxVisiblePostCntInPage, int pageNumber, String searchType, String searchValue,
                                             Ref<Integer> totalPageRef);
}
