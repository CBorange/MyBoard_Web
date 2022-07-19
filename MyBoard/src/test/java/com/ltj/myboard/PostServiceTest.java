package com.ltj.myboard;

import com.ltj.myboard.dto.FilteredPost;
import com.ltj.myboard.service.serviceinterface.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    public void 제목검색조건으로_게시글조회(){
        List<FilteredPost> findResult = postService.findPost_UserParam(115,"Tttle", "21","ModifyDay","ASC");
        Assertions.assertTrue(findResult.stream().count() > 0);
    }

    @Test
    public void 내용검색조건으로_게시글조회(){

    }

    @Test
    public void 댓글검색조건으로_게시글조회(){

    }

    @Test
    public void 닉네임검색조건으로_게시글조회(){

    }

    @Test
    public void 페이지넘버_조건으로_게시글조회(){

    }
}
