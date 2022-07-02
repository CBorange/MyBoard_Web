package com.ltj.myboard.service;

import com.ltj.myboard.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> findPostByID(int postID);
    List<Post> findPostByBoardID(int boardID);
    List<Post> findPostByWriterID(String writerID);
}
