package com.ltj.myboard.util;

import java.util.List;

// 페이지 및 페이지 세션(그룹) 계산 유틸
public class Paginator {
    public static int getCurSessionByCurPage(int curPage, int maxVisiblePageInSession){
        int curSession = curPage / maxVisiblePageInSession;
        if(curPage % maxVisiblePageInSession > 0)
            curSession += 1;
        return curSession;
    }
}
