package com.ltj.myboard.service;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.board.FilteredPost;
import com.ltj.myboard.dto.post.SubmitPostData;
import com.ltj.myboard.repository.FilteredPostRepository;
import com.ltj.myboard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class PostService{

    private final PostRepository postRepository;
    private final FilteredPostRepository filteredPostRepository;

    public Optional<Post> findPostByID(int postID) {
        return postRepository.findPostByID(postID);
    }

    public List<Post> findPostByBoardID(int boardID) {
        return postRepository.findAllPostByBoardID(boardID);
    }

    public List<Post> findPostByWriterID(String writerID) {
        return postRepository.findPostByWriterID(writerID);
    }

    public List<FilteredPost> findPost_UserParam(int boardID, String searchMethod, String searchCondition, String sortOrderTarget,
                                                 String orderByMethod) {
        List<FilteredPost> selectRet = new ArrayList<FilteredPost>();

        // 검색 Method에 따른 검색 쿼리 수행
        switch (searchMethod){
            case "Title":
                selectRet = filteredPostRepository.findPost_UseSearch_Title(boardID, searchCondition, sortOrderTarget, orderByMethod);
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

    public List<FilteredPost> filterPostDataInCurPage(List<FilteredPost> sourceList, int curPage,
                                                      int maxVisiblePostCountInPage) {
        // 현재 페이지의 첫번째, 마지막 게시글 범위 지정
        int pageStartRowNo = (curPage - 1) * maxVisiblePostCountInPage;
        int pageEndRowNo = curPage * maxVisiblePostCountInPage;

        // 현재 페이지의 범위에 해당하는 게시글을 Stream으로 filtering 한다
        List<FilteredPost> filteredPostList = sourceList.stream().filter((source) -> {
            if(source.getOrderedPostNo() >= pageStartRowNo &&
               source.getOrderedPostNo() <= pageEndRowNo)
                return true;
            return false;
        }).collect(Collectors.toList());

        return filteredPostList;
    }

    public Post insertPost(String title, String content, int boardID, String writerID) throws SQLException {
        int generatedRowKey = postRepository.insertPost(title, content, boardID, writerID);
        Optional<Post> newPost = findPostByID(generatedRowKey);
        return newPost.orElseThrow(() -> {
            return new SQLException("PostService : insertPost Error, can't not found from generatedKey = " + generatedRowKey);
        });
    }
}
