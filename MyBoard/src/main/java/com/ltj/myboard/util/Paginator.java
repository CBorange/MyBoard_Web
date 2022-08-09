package com.ltj.myboard.util;

import java.util.List;

// 페이지 및 페이지 세션 계산 유틸
public class Paginator {
    public static int getPageCount(long sourceCount, int maxVisibleCountInSinglePage){
        int pageCount = (int)(sourceCount / maxVisibleCountInSinglePage);
        if((sourceCount % maxVisibleCountInSinglePage > 0) ||
           sourceCount == 0){
            pageCount += 1;
        }

        return pageCount;
    }

    public static int getCurSessionByCurPage(int curPage, int maxVisiblePageInSession){
        int curSession = curPage / maxVisiblePageInSession;
        if(curPage % maxVisiblePageInSession > 0)
            curSession += 1;
        return curSession;
    }
}
