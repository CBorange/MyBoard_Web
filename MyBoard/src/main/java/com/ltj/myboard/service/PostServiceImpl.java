package com.ltj.myboard.service;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.FilteredPost;
import com.ltj.myboard.repository.FilteredPostRepository;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FilteredPostRepository filteredPostRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, FilteredPostRepository filteredPostRepository){
        this.postRepository = postRepository;
        this.filteredPostRepository = filteredPostRepository;
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
    public List<FilteredPost> findPost_UserParam(int boardID, String searchMethod, String searchCondition, String sortOrderTarget,
                                                 String orderByMethod) {
        List<FilteredPost> selectRet = new ArrayList<FilteredPost>();

        // 검색 Method에 따른 검색 쿼리 수행
        switch (searchMethod){
            case "Title":
                filteredPostRepository.findPost_UseSearch_Title(boardID, searchCondition, sortOrderTarget, orderByMethod);
                break;
            case "Content":
                break;
            case "Comment":
                break;
            case "Nickname":
                break;
        }

        return selectRet;
    }
}
