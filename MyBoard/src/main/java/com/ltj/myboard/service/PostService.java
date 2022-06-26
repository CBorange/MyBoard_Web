package com.ltj.myboard.service;

import com.ltj.myboard.domain.Post;

import java.util.List;

public interface PostService {
    List<Post> findPostByBoardID(int boardID);
    List<Post> findPostByWriterID(String writerID);
}
