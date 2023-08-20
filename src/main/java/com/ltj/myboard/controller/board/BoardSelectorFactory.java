package com.ltj.myboard.controller.board;

import com.ltj.myboard.domain.BoardTypeDefiner;
import com.ltj.myboard.service.PostService;

/**
 * BoardSelector 심플 팩토리*/
public class BoardSelectorFactory {
    public BoardSelector createInstance(PostService postService, BoardTypeDefiner boardType){
        BoardSelector newSelector = null;
        switch (boardType){
            case Best:
                newSelector = new BestBoardSelector(postService);
                break;
            case Announcement:
            case Common:
                newSelector = new CommonBoardSelector(postService);
                break;
        }
        return newSelector;
    }
}
