package com.ltj.myboard.service.serviceinterface;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.FilteredPost;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> findPostByID(int postID);
    List<Post> findPostByBoardID(int boardID);
    List<Post> findPostByWriterID(String writerID);
    List<FilteredPost> findPost_UserParam(int boardID, String searchMethod, String searchCondition, String sortOrderTarget,
                                          String orderByMethod);
}
