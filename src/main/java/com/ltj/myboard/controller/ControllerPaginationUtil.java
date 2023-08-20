package com.ltj.myboard.controller;

import com.ltj.myboard.util.Paginator;
import org.springframework.ui.Model;

/**
 * 페이지별 조회 처리가 필요한 API(Get) Controller에서 공통으로 처리되는 부분 모음 Util*/
public class ControllerPaginationUtil {
    /**
     * 현재 Page, Page 수, 현재 Page 등 페이지 관련 Model 생성 및 추가
     * @param model 화면 Rendering 시 사용된 MVC model 객체\
     * @param pageDataList 조회된 현재 페이지에 표시되어야 할 목적 데이터(현제 페이지 게시글 목록, 현재 페이지 댓글 목록 등)
     * @param rawPageCount 조회 조건에 따라 조회된 목적 데이터의 총 페이지 개수
     * @param curPageNo 조회하려는 현재 페이지 번호*/
    public void makeSimplePageModel(Model model, Object pageDataList, int rawPageCount, int curPageNo){

    }

    /**
     * 현재 Page 정보 및 현재 UI 상에서 한번에 이동가능한 Page 영역(이하 Session이라 함) Model 추가
     * @param model 화면 Rendering 시 사용된 MVC model 객체\
     * @param pageDataList 조회된 현재 페이지에 표시되어야 할 목적 데이터(현제 페이지 게시글 목록, 현재 페이지 댓글 목록 등)
     * @param rawPageCount 조회 조건에 따라 조회된 목적 데이터의 총 페이지 개수
     * @param curPageNo 조회하려는 현재 페이지 번호
     * @param maxVisiblePageCntInCurSession 현재 Session에 표시되어야 하는 최대 페이지 개수*/
    public void makePageModelWithSession(Model model, Object pageDataList, int rawPageCount, int curPageNo,
                                         int maxVisiblePageCntInCurSession){
        // 페이지 개수 가공
        int pageCount = rawPageCount;
        if(pageCount == 0)
            pageCount = 1;
        if(curPageNo > pageCount)
            throw new IllegalArgumentException(curPageNo + " page is out of bound");

        // 현재 페이지의 세션 구하기
        int curSession = Paginator.getCurSessionByCurPage(curPageNo, maxVisiblePageCntInCurSession);
        int endPageNoInCurSession = curSession * maxVisiblePageCntInCurSession;
        int startPageNoInCurSession = endPageNoInCurSession - (maxVisiblePageCntInCurSession - 1);
        if(pageCount < endPageNoInCurSession) endPageNoInCurSession = pageCount;

        // 현재 페이지 정보 Model에 추가
        model.addAttribute("pageDataList", pageDataList);
        model.addAttribute("curPageNo", curPageNo);
        model.addAttribute("endPageNoInCurSession", endPageNoInCurSession);
        model.addAttribute("startPageNoInCurSession", startPageNoInCurSession);
        model.addAttribute("pageCount", pageCount);
    }
}
