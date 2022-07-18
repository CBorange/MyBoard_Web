package com.ltj.myboard.service;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.OrderedPost;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.repository.jdbc.JDBC_PostRepository;
import com.ltj.myboard.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public Optional<Post> findPostByID(int postID) {
        return postRepository.findPostByID(postID);
    }

    @Override
    public List<Post> findPostByBoardID(int boardID) {
        return postRepository.findAllPostByBoardID(boardID);
    }

    @Override
    public List<Post> findPostByWriterID(String writerID) {
        return postRepository.findPostByWriterID(writerID);
    }

    @Override
    public List<OrderedPost> findPost_UserParam(int boardID, String searchMethod, String searchCondition, String sortOrderTarget) {
        List<OrderedPost> selectRet = new ArrayList<OrderedPost>();
        List<Post> unorderedPostList = new ArrayList<Post>();

        // 검색 Method에 따른 검색 쿼리 수행
        switch (searchMethod){
            case "Title":
                unorderedPostList = postRepository.findPost_UseSearch_Title(boardID, searchCondition);
                unorderedPostList = unorderedPostList.stream().sorted(Comparator.comparing(Post::getTitle))
                        .collect(Collectors.toList());
                break;
            case "Content":
                break;
            case "Comment":
                break;
            case "Nickname":
                break;
        }

        // 정렬 Target에 따른 정렬 기능 수행


        // 정렬된 순서대로 DTO 생성
        int orderNo = 0;
        for(Post post : unorderedPostList){
            OrderedPost orderedPost = new OrderedPost();
            orderedPost.setOrderedPostNo(orderNo);
            orderedPost.setPostData(post);
            selectRet.add(orderedPost);
        }

        return selectRet;
    }
}
