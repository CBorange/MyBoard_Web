package com.ltj.myboard.service;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.board.FilteredPost;
import com.ltj.myboard.repository.FilteredPostRepository;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<FilteredPost> filterPostDataOnCurPage(List<FilteredPost> sourceList, int pageCount, int curPage, int maxVisiblePostCountInPage) {
        // 현재 페이지의 첫번째, 마지막 게시글 범위 지정
        int pageStartRowNo = (curPage - 1) * maxVisiblePostCountInPage;
        int pageEndRowNo = curPage * maxVisiblePostCountInPage;

        // 게시글의 범위에 해당하는 게시글을 Stream으로 filtering 한다
        List<FilteredPost> filteredPostList = sourceList.stream().filter((source) -> {
            if(source.getOrderedPostNo() >= pageStartRowNo &&
               source.getOrderedPostNo() <= pageEndRowNo)
                return true;
            return false;
        }).collect(Collectors.toList());

        return filteredPostList;
    }

    @Override
    public int getPageCountOnPostList(List<FilteredPost> sourceList, int maxVisiblePostCountInPage) {
        long sourceCount = sourceList.stream().count();

        int pageCount = (int)(sourceCount / maxVisiblePostCountInPage);
        if(sourceCount % maxVisiblePostCountInPage > 0)
            pageCount += 1;
        return pageCount;
    }

    @Override
    public int getCurSessionByCurPage(int curPage, int maxVisibleSessionCountInPage){
        int curSession = curPage / maxVisibleSessionCountInPage;
        if(curPage % maxVisibleSessionCountInPage > 0)
            curSession += 1;
        return curSession;
    }
}
