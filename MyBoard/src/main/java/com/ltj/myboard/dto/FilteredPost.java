package com.ltj.myboard.dto;

import com.ltj.myboard.domain.Post;

// 게시글 목록 표시할 때 사용하는 dto Class
// 검색 조건(제목, 내용, 댓글 등)및
// 정렬 메소드(일자, 추천수, 제목 등)에 따라 정렬된 Post의 정보를 담는다.
public class FilteredPost {
    private int OrderedPostNo;

    private Post PostData;

    public int getOrderedPostNo() {
        return OrderedPostNo;
    }

    public void setOrderedPostNo(int orderedPostNo) {
        OrderedPostNo = orderedPostNo;
    }

    public Post getPostData() {
        return PostData;
    }

    public void setPostData(Post postData) {
        PostData = postData;
    }
}
